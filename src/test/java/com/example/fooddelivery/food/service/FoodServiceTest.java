package com.example.fooddelivery.food.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.food.dto.FoodRequestDto;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

	@Mock
	private FoodRepository foodRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private FoodService foodService;

	@Test
	void 정상적으로_음식_생성() {
		//given
		FoodRequestDto requestDto = new FoodRequestDto("음식이름", 10000);
		Long restaurantId = 1L;

		given(restaurantRepository.findById(any())).willReturn(Optional.of(new Restaurant(restaurantId, null,
			0, 0, null, null, null)));
		given(foodRepository.save(any())).willReturn(new Food(1L, requestDto.getName(),
			requestDto.getPrice(), null));

		//when
		Long result = foodService.createFood(requestDto, restaurantId);

		//then
		assertThat(result).isEqualTo(1L);
	}

	@Test
	void 식당없음으로인한_음식_생성_실패() {
		//given
		FoodRequestDto requestDto = new FoodRequestDto("음식이름", 10000);
		Long restaurantId = 1L;

		given(restaurantRepository.findById(any())).willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> foodService.createFood(requestDto, restaurantId))
			.isInstanceOf(NotFoundException.class);

	}

}
