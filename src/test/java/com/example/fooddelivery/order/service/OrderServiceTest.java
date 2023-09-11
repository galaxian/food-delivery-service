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
import com.example.fooddelivery.order.dto.MenuQuantityReqDto;
import com.example.fooddelivery.order.repository.OrderRepository;
import com.example.fooddelivery.ordermenu.repository.OrderMenuRepository;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	private static final Restaurant restaurant = new Restaurant(1L, "치킨집", 10000,
		3000, null, null, null);
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
		MenuQuantityReqDto menuQuantityReqDto = new MenuQuantityReqDto(1L, 1, 20000);
		List<MenuQuantityReqDto> menuReqList = new ArrayList<>();
		menuReqList.add(menuQuantityReqDto);
		CreateOrderReqDto createOrderReqDto = new CreateOrderReqDto(menuReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(restaurant));
		given(orderRepository.save(any()))
			.willReturn(new Order(1L, OrderStatus.WAITING, now, restaurant));
		given(menuRepository.findById(any()))
			.willReturn(Optional.of(new Menu(1L, "양념치킨", 20000, "비법소스로 만든 치킨"
				, true, MenuStatus.SALE, restaurant)));

		//when
		//then
		assertDoesNotThrow(() -> orderService.createOrder(createOrderReqDto, restaurantId));

	}

	@Test
	void 식당_NotFound_주문_생성_실패() {
		//given
		MenuQuantityReqDto menuQuantityReqDto = new MenuQuantityReqDto(1L, 1, 20000);
		List<MenuQuantityReqDto> menuReqList = new ArrayList<>();
		menuReqList.add(menuQuantityReqDto);
		CreateOrderReqDto createOrderReqDto = new CreateOrderReqDto(menuReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> orderService.createOrder(createOrderReqDto, restaurantId))
			.isInstanceOf(NotFoundException.class);
	}

}
