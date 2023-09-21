package com.example.fooddelivery.order.service;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.order.domain.Order;
import com.example.fooddelivery.order.dto.CreateOrderReqDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Long createOrder(CreateOrderReqDto reqDto, Long restaurantsId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantsId).orElseThrow(
                () -> new NotFoundException("식당을 찾을 수 없습니다.")
        );
        Order saveOrder = orderRepository.save(Order.createOrder(LocalDateTime.now(), restaurant));

        List<MenuQuantityReqDto> menuQuantityDtoList = reqDto.getMenuReqList();
        for (MenuQuantityReqDto req : menuQuantityDtoList) {
            Menu menu = menuRepository.findById(req.getId()).orElseThrow(
                    () -> new NotFoundException("메뉴를 찾을 수 없습니다.")
            );

            OrderMenu orderMenu = OrderMenu.createOrderMenu(req.getQuantity(), req.getPrice(), saveOrder, menu);
            orderMenuRepository.save(orderMenu);
        }

        return saveOrder.getId();
    }

    @Transactional
    public OrderDetailResDto findOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("주문을 찾을 수 없습니다.")
        );

        List<OrderMenu> orderMenuList = orderMenuRepository.findByOrderId(id);

        int totalPrice = 0;
        for (OrderMenu orderMenu: orderMenuList) {
            totalPrice += orderMenu.sumTotalPrice();
        }

        List<OrderMenuResDto> resDtoList = orderMenuList.stream().map(OrderMenuResDto::new).collect(Collectors.toList());

        return new OrderDetailResDto(order, totalPrice, resDtoList);
    }

    @Transactional
    public List<OrderDetailResDto> findAllOrder(Long restaurantId) {
        List<Order> orderList = orderRepository.findAllByRestaurantId(restaurantId);
        List<OrderDetailResDto> resDtoList = new ArrayList<>();

        for (Order order: orderList) {
            List<OrderMenu> orderMenuList = orderMenuRepository.findByOrderId(order.getId());

            int totalPrice = 0;
            for (OrderMenu orderMenu: orderMenuList) {
                totalPrice += orderMenu.sumTotalPrice();
            }

            List<OrderMenuResDto> menuList = orderMenuList.stream().map(OrderMenuResDto::new).collect(Collectors.toList());
            resDtoList.add(new OrderDetailResDto(order, totalPrice, menuList));
        }
        return resDtoList;
    }

    @Transactional
    public void updateOrder(List<MenuQuantityReqDto> reqDto, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException("주문을 찾을 수 없습니다.")
        );

        if (reqDto != null) {
            orderMenuRepository.deleteAllByOrderId(orderId);

            for (MenuQuantityReqDto req : reqDto) {
                Menu menu = menuRepository.findById(req.getId()).orElseThrow(
                        () -> new NotFoundException("메뉴를 찾을 수 없습니다.")
                );

                OrderMenu orderMenu = OrderMenu.createOrderMenu(req.getQuantity(), req.getPrice(), order, menu);
                orderMenuRepository.save(orderMenu);
            }
        }
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
