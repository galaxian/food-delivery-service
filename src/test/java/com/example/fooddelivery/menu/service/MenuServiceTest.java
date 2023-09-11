package com.example.fooddelivery.menu.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.domain.MenuStatus;
import com.example.fooddelivery.menu.dto.CreateMenuReqDto;
import com.example.fooddelivery.menu.dto.FoodQuantityReqDto;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.menufood.repository.MenuFoodRepository;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	private static final Restaurant restaurant = new Restaurant(1L, "치킨집", 10000,
		3000, null, null, null);

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private FoodRepository foodRepository;

	@Mock
	private MenuFoodRepository menuFoodRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private MenuService menuService;

	@Test
	void 메뉴_생성_성공() {
		//given
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto menuReq = new CreateMenuReqDto("양념치킨", 20000, "특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(restaurant));
		given(menuRepository.save(any()))
			.willReturn(new Menu(1L,menuReq.getName(), menuReq.getPrice(), menuReq.getDescribe()
				,true, MenuStatus.SALE, restaurant));
		given(foodRepository.findById(any()))
			.willReturn(Optional.of(new Food(1L, "치킨", 20000, restaurant)))
			.willReturn(Optional.of(new Food(2L, "콜라", 3300, restaurant)));

		//when
		//then
		assertDoesNotThrow(() -> menuService.createMenu(menuReq, restaurantId));

	}

	@Test
	void 식당_NotFound_메뉴_생성_실패() {
		//given
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto menuReq = new CreateMenuReqDto("양념치킨", 20000, "특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> menuService.createMenu(menuReq, restaurantId))
			.isInstanceOf(NotFoundException.class);

	}

	@Test
	void 음식_NotFound_메뉴_생성_실패() {
		//given
		FoodQuantityReqDto foodReq1 = new FoodQuantityReqDto(1L, 1);
		FoodQuantityReqDto foodReq2 = new FoodQuantityReqDto(2L, 1);
		List<FoodQuantityReqDto> foodReqList = new ArrayList<>();
		foodReqList.add(foodReq1);
		foodReqList.add(foodReq2);
		CreateMenuReqDto menuReq = new CreateMenuReqDto("양념치킨", 20000, "특급소스로 만든 치킨", foodReqList);

		Long restaurantId = 1L;

		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(restaurant));
		given(menuRepository.save(any()))
			.willReturn(new Menu(1L,menuReq.getName(), menuReq.getPrice(), menuReq.getDescribe()
				,true, MenuStatus.SALE, restaurant));
		given(foodRepository.findById(any()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> menuService.createMenu(menuReq, restaurantId))
			.isInstanceOf(NotFoundException.class);
	}

}
