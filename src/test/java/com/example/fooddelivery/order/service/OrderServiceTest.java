package com.example.fooddelivery.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.domain.MenuStatus;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.order.domain.Order;
import com.example.fooddelivery.order.domain.OrderStatus;
import com.example.fooddelivery.order.dto.CreateOrderReqDto;
import com.example.fooddelivery.order.dto.GetAllOrderByPhoneReqDto;
import com.example.fooddelivery.order.dto.MenuQuantityReqDto;
import com.example.fooddelivery.order.dto.OrderDetailResDto;
import com.example.fooddelivery.order.repository.OrderRepository;
import com.example.fooddelivery.ordermenu.domain.OrderMenu;
import com.example.fooddelivery.ordermenu.repository.OrderMenuRepository;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	private static final Owner OWNER = new Owner(1L, "주인", "비밀번호", "salt");
	private static final Restaurant RESTAURANT = new Restaurant(1L, "치킨집", 10000,
		3000, OWNER, null, null, null);
	private final LocalDateTime now = LocalDateTime.now();

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderMenuRepository orderMenuRepository;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	void 주문_생성_성공() {
		//given
		String phoneNumber = "010-1234-5678";
		MenuQuantityReqDto menuQuantityReqDto = new MenuQuantityReqDto(1L, 1);
		List<MenuQuantityReqDto> menuReqList = new ArrayList<>();
		menuReqList.add(menuQuantityReqDto);
		CreateOrderReqDto createOrderReqDto = new CreateOrderReqDto(phoneNumber, menuReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));
		given(orderRepository.save(any()))
			.willReturn(new Order(1L, OrderStatus.WAITING, now, phoneNumber, RESTAURANT));
		given(menuRepository.findById(any()))
			.willReturn(Optional.of(new Menu(1L, "양념치킨", 20000, "비법소스로 만든 치킨"
				, true, MenuStatus.SALE, RESTAURANT)));

		//when
		//then
		assertDoesNotThrow(() -> orderService.createOrder("주인", createOrderReqDto, restaurantId));

	}

	@Test
	void 식당_NotFound_주문_생성_실패() {
		//given
		String phoneNumber = "010-1234-5678";
		MenuQuantityReqDto menuQuantityReqDto = new MenuQuantityReqDto(1L, 1);
		List<MenuQuantityReqDto> menuReqList = new ArrayList<>();
		menuReqList.add(menuQuantityReqDto);
		CreateOrderReqDto createOrderReqDto = new CreateOrderReqDto(phoneNumber, menuReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.empty());
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertThatThrownBy(() -> orderService.createOrder("주인", createOrderReqDto, restaurantId))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	void 메뉴_NotFound_주문_생성_실패() {
		//given
		String phoneNumber = "010-1234-5678";
		MenuQuantityReqDto menuQuantityReqDto = new MenuQuantityReqDto(1L, 1);
		List<MenuQuantityReqDto> menuReqList = new ArrayList<>();
		menuReqList.add(menuQuantityReqDto);
		CreateOrderReqDto createOrderReqDto = new CreateOrderReqDto(phoneNumber, menuReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));
		given(orderRepository.save(any()))
			.willReturn(new Order(1L, OrderStatus.WAITING, now, phoneNumber, RESTAURANT));
		given(menuRepository.findById(any()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> orderService.createOrder("주인", createOrderReqDto, restaurantId))
			.isInstanceOf(NotFoundException.class);

	}

	@Test
	void 메뉴_단일_조회_성공() {
		//given
		Long orderId = 1L;
		String phoneNumber = "010-1234-5678";
		Order order = new Order(orderId, OrderStatus.WAITING, now, phoneNumber, RESTAURANT);
		Menu menu1 = new Menu(1L, "양념치킨", 20000, "비법소스로 만든 치킨"
			, true, MenuStatus.SALE, RESTAURANT);
		Menu menu2 = new Menu(2L, "후라이드치킨", 15000, "잘 튀긴 치킨"
			, true, MenuStatus.SALE, RESTAURANT);

		OrderMenu orderMenu1 = new OrderMenu(1L, 1, 20000, order, menu1);
		OrderMenu orderMenu2 = new OrderMenu(2L, 2, 15000, order, menu2);
		List<OrderMenu> orderMenuList = new ArrayList<>();
		orderMenuList.add(orderMenu1);
		orderMenuList.add(orderMenu2);

		given(orderRepository.findById(any()))
			.willReturn(Optional.of(order));
		given(orderMenuRepository.findByOrderId(any()))
			.willReturn(orderMenuList);

		//when
		OrderDetailResDto result = orderService.findOrder(orderId);

		//then
		assertThat(result.getId()).isEqualTo(orderId);
		assertThat(result.getOrderTime()).isBefore(LocalDateTime.now());
		assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus());
		assertThat(result.getTotalPrice()).isEqualTo(orderMenu1.getPrice() * orderMenu1.getQuantity()
			+ orderMenu2.getPrice() * orderMenu2.getQuantity());
	}

	@Test
	void 주문_NotFound_주문_단일_조회_실패() {
		//given
		Long orderId = 1L;

		given(orderRepository.findById(any()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> orderService.findOrder(orderId))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	void 식당_주문_목록_조회_성공() {
		//given
		String phoneNumber = "010-1234-5678";
		Order order1 = new Order(1L, OrderStatus.WAITING, now, phoneNumber, RESTAURANT);
		Menu menu1 = new Menu(1L, "양념치킨", 20000, "비법소스로 만든 치킨"
			, true, MenuStatus.SALE, RESTAURANT);
		Menu menu2 = new Menu(2L, "후라이드치킨", 15000, "잘 튀긴 치킨"
			, true, MenuStatus.SALE, RESTAURANT);

		Order order2 = new Order(2L, OrderStatus.WAITING, now, phoneNumber, RESTAURANT);

		List<Order> orderList = new ArrayList<>();
		orderList.add(order1);
		orderList.add(order2);

		OrderMenu orderMenu1 = new OrderMenu(1L, 1, 20000, order1, menu1);
		OrderMenu orderMenu2 = new OrderMenu(2L, 2, 15000, order1, menu2);
		List<OrderMenu> orderMenuList1 = new ArrayList<>();
		orderMenuList1.add(orderMenu1);
		orderMenuList1.add(orderMenu2);

		OrderMenu orderMenu3 = new OrderMenu(3L, 2, 20000, order1, menu1);
		OrderMenu orderMenu4 = new OrderMenu(4L, 1, 15000, order1, menu2);
		List<OrderMenu> orderMenuList2 = new ArrayList<>();
		orderMenuList2.add(orderMenu3);
		orderMenuList2.add(orderMenu4);

		given(orderRepository.findAllByRestaurantId(any()))
			.willReturn(orderList);
		given(orderMenuRepository.findByOrderId(any()))
			.willReturn(orderMenuList1)
			.willReturn(orderMenuList2);

		//when
		List<OrderDetailResDto> result = orderService.findAllOrder(RESTAURANT.getId());

		//then
		assertThat(result.get(0).getId()).isEqualTo(order1.getId());
		assertThat(result.get(1).getId()).isEqualTo(order2.getId());
		assertThat(result.size()).isEqualTo(orderList.size());
	}

	@Test
	void 주문_목록_조회_성공byPhoneNuber() {
		//given
		String phoneNumber = "010-1234-5678";
		Order order1 = new Order(1L, OrderStatus.WAITING, now, phoneNumber, RESTAURANT);
		Menu menu1 = new Menu(1L, "양념치킨", 20000, "비법소스로 만든 치킨"
			, true, MenuStatus.SALE, RESTAURANT);
		Menu menu2 = new Menu(2L, "후라이드치킨", 15000, "잘 튀긴 치킨"
			, true, MenuStatus.SALE, RESTAURANT);

		Order order2 = new Order(2L, OrderStatus.WAITING, now, phoneNumber, RESTAURANT);

		List<Order> orderList = new ArrayList<>();
		orderList.add(order1);
		orderList.add(order2);

		OrderMenu orderMenu1 = new OrderMenu(1L, 1, 20000, order1, menu1);
		OrderMenu orderMenu2 = new OrderMenu(2L, 2, 15000, order1, menu2);
		List<OrderMenu> orderMenuList1 = new ArrayList<>();
		orderMenuList1.add(orderMenu1);
		orderMenuList1.add(orderMenu2);

		OrderMenu orderMenu3 = new OrderMenu(3L, 2, 20000, order1, menu1);
		OrderMenu orderMenu4 = new OrderMenu(4L, 1, 15000, order1, menu2);
		List<OrderMenu> orderMenuList2 = new ArrayList<>();
		orderMenuList2.add(orderMenu3);
		orderMenuList2.add(orderMenu4);

		given(orderRepository.findAllByPhoneNumber(any()))
			.willReturn(orderList);
		given(orderMenuRepository.findByOrderId(any()))
			.willReturn(orderMenuList1)
			.willReturn(orderMenuList2);

		//when
		List<OrderDetailResDto> result = orderService.findAllOrderByPhoneNumber(
			new GetAllOrderByPhoneReqDto(phoneNumber)
		);

		//then
		assertThat(result.get(0).getId()).isEqualTo(order1.getId());
		assertThat(result.get(1).getId()).isEqualTo(order2.getId());
		assertThat(result.size()).isEqualTo(orderList.size());
	}

	@Test
	void 주문_NotFound_주문_수정_실패() {
		//given
		Long orderId = 1L;
		Long restaurantId = 1L;

		MenuQuantityReqDto menuQuantityReqDto = new MenuQuantityReqDto(1L, 1);
		List<MenuQuantityReqDto> menuReqList = new ArrayList<>();
		menuReqList.add(menuQuantityReqDto);

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));
		given(orderRepository.findById(any()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> orderService.updateOrder("주인", restaurantId, menuReqList ,orderId))
			.isInstanceOf(NotFoundException.class);
	}

}
