package com.example.fooddelivery.order.service;

import com.example.fooddelivery.common.exception.BadRequestException;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.common.exception.UnauthorizedException;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.order.domain.Order;
import com.example.fooddelivery.order.dto.CreateOrderReqDto;
import com.example.fooddelivery.order.dto.GetAllOrderByPhoneReqDto;
import com.example.fooddelivery.order.dto.MenuQuantityReqDto;
import com.example.fooddelivery.order.dto.OrderDetailResDto;
import com.example.fooddelivery.order.dto.OrderMenuResDto;
import com.example.fooddelivery.order.repository.OrderRepository;
import com.example.fooddelivery.ordermenu.domain.OrderMenu;
import com.example.fooddelivery.ordermenu.repository.OrderMenuRepository;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, OrderMenuRepository orderMenuRepository, RestaurantRepository restaurantRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderMenuRepository = orderMenuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Long createOrder(String identifier, CreateOrderReqDto reqDto, Long restaurantsId) {
        Restaurant restaurant = findRestaurantById(restaurantsId);
        validateOwner(identifier, restaurant);
        String phoneNumber = deleteDashPhoneNumber(reqDto.getPhoneNumber());
        Order order = Order.createOrder(restaurant, phoneNumber);
        Order saveOrder = orderRepository.save(order);

        List<OrderMenu> orderMenuList = makeOrderMenuList(reqDto.getMenuReqList(), saveOrder);
        orderMenuRepository.saveAll(orderMenuList);
        return saveOrder.getId();
    }

    private void validateOwner(String identifier, Restaurant restaurant) {
        if (!restaurant.isOwner(identifier)) {
            throw new UnauthorizedException("식당 주인만 사용할 수 있는 기능입니다.");
        }
    }

    private List<OrderMenu> makeOrderMenuList(List<MenuQuantityReqDto> reqDtoList, Order order) {
        return reqDtoList.stream()
            .map((req) -> makeOrderMenu(req, order))
            .collect(Collectors.toList());
    }

    private OrderMenu makeOrderMenu(MenuQuantityReqDto req, Order order) {
        Menu menu = findMenuById(req.getId());
        return OrderMenu.createOrderMenu(req.getQuantity(),
            menu.getPrice(), order, menu);
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(
            () -> new NotFoundException("메뉴를 찾을 수 없습니다.")
        );
    }

    private Restaurant findRestaurantById(Long restaurantsId) {
        return restaurantRepository.findById(restaurantsId).orElseThrow(
            () -> new NotFoundException("식당을 찾을 수 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    public OrderDetailResDto findOrder(Long id) {
        Order order = findOrderById(id);
        List<OrderMenu> orderMenuList = findOrderMenusByOrderId(id);
        int totalPrice = getTotalPrice(orderMenuList);
        List<OrderMenuResDto> resDtoList = makeOrderMenuResList(orderMenuList);
        return new OrderDetailResDto(order, totalPrice, resDtoList);
    }

    private int getTotalPrice(List<OrderMenu> orderMenuList) {
        return orderMenuList.stream()
            .map(OrderMenu::getTimesQuantityAndPrice)
            .reduce(0, Integer::sum);
    }

    private List<OrderMenuResDto> makeOrderMenuResList(List<OrderMenu> orderMenuList) {
        return orderMenuList.stream()
            .map(OrderMenuResDto::new)
            .collect(Collectors.toList());
    }

    private List<OrderMenu> findOrderMenusByOrderId(Long id) {
        return orderMenuRepository.findByOrderId(id);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(
            () -> new NotFoundException("주문을 찾을 수 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    public List<OrderDetailResDto> findAllOrder(String identifier, Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        validateOwner(identifier, restaurant);
        List<Order> orderList = findOrdersByRestaurantId(restaurantId);
        return makeOrderDetailResDtoList(orderList);
    }

    private List<OrderDetailResDto> makeOrderDetailResDtoList(List<Order> orderList) {
        return orderList.stream()
            .map(this::makeOrderDetailResDto)
            .collect(Collectors.toList());
    }

    private OrderDetailResDto makeOrderDetailResDto(Order order) {
        List<OrderMenu> orderMenuList = findOrderMenusByOrderId(order.getId());
        return new OrderDetailResDto(order,
            getTotalPrice(orderMenuList),
            makeOrderMenuResList(orderMenuList));
    }

    private List<Order> findOrdersByRestaurantId(Long restaurantId) {
        return orderRepository.findAllByRestaurantId(restaurantId);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailResDto> findAllOrderByPhoneNumber(GetAllOrderByPhoneReqDto reqDto) {
        String phoneNumber = deleteDashPhoneNumber(reqDto.getPhoneNumber());
        System.out.println(phoneNumber);
        List<Order> orderList = orderRepository.findAllByPhoneNumber(phoneNumber);
        return makeOrderDetailResDtoList(orderList);
    }

    private String deleteDashPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("-", "");
    }

    @Transactional
    public void updateOrder(String identifier, Long restaurantsId, List<MenuQuantityReqDto> reqDto,
        Long orderId) {
        Restaurant restaurant = findRestaurantById(restaurantsId);
        validateOwner(identifier, restaurant);
        validateMenuEmpty(reqDto);
        Order order = findOrderById(orderId);
        orderMenuRepository.deleteAllByOrderId(orderId);
        List<OrderMenu> orderMenuList = makeOrderMenuList(reqDto, order);
        orderMenuRepository.saveAll(orderMenuList);
    }

    private void validateMenuEmpty(List<MenuQuantityReqDto> reqDto) {
        if (reqDto.isEmpty()) {
            throw new BadRequestException("주문할 음식이 지정되지 않았습니다.");
        }
    }

    @Transactional
    public void deleteOrder(String identifier, Long restaurantsId, Long orderId) {
        Restaurant restaurant = findRestaurantById(restaurantsId);
        validateOwner(identifier, restaurant);
        orderRepository.deleteById(orderId);
    }
}
