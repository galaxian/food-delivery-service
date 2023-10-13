package com.example.fooddelivery.food.service;

import static org.assertj.core.api.Assertions.*;
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
import com.example.fooddelivery.food.dto.FoodRequestDto;
import com.example.fooddelivery.food.dto.FoodResponseDto;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.owner.domain.Owner;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

	private static final Owner OWNER = new Owner(1L, "주인", "비밀번호", "salt");
	private static final Restaurant RESTAURANT = new Restaurant(1L, "식당이름",
		10000, 3000, OWNER, null, null, null);


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

		given(restaurantRepository.findById(any())).willReturn(Optional.of(RESTAURANT));
		given(foodRepository.save(any())).willReturn(new Food(1L, requestDto.getName(),
			requestDto.getPrice(), null));

		//when
		Long result = foodService.createFood("주인", requestDto, restaurantId);

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
		assertThatThrownBy(() -> foodService.createFood("주인", requestDto, restaurantId))
			.isInstanceOf(NotFoundException.class);

	}

	@Test
	void 음식_단일_조회_성공() {
		//given
		Long restaurantId = 1L;
		Long foodId = 1L;
		String name = "음식이름";
		int price = 1000;

		given(foodRepository.findById(any()))
			.willReturn(Optional.of(new Food(foodId, name, price, RESTAURANT)));
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		FoodResponseDto result = foodService.findFood("주인", restaurantId, foodId);

		//then
		assertThat(result.getId()).isEqualTo(foodId);
		assertThat(result.getName()).isEqualTo(name);
		assertThat(result.getPrice()).isEqualTo(price);

	}

	@Test
	void 음식_NotFound_단일_조회_실패() {
		//given
		Long foodId = 1L;
		Long restaurantId = 1L;

		given(foodRepository.findById(any()))
			.willReturn(Optional.empty());
		given(restaurantRepository.findById(any()))
			.willReturn(Optional.of(RESTAURANT));

		//when
		//then
		assertThatThrownBy(() -> foodService.findFood("주인", restaurantId, foodId))
			.isInstanceOf(NotFoundException.class);

	}

	@Test
	void 식당의_음식_목록_조회_성공() {
		//given
		Long foodId1 = 1L;
		String name1 = "음식이름";
		int price1 = 1000;

		Long foodId2 = 2L;
		String name2 = "음식2";
		int price2 = 2000;

		List<Food> foodList = new ArrayList<>();
		foodList.add(new Food(foodId1, name1, price1, RESTAURANT));
		foodList.add(new Food(foodId2, name2, price2, RESTAURANT));

		given(foodRepository.findAllByRestaurantId(any())).willReturn(foodList);
		given(restaurantRepository.findById(any())).willReturn(Optional.of(RESTAURANT));

		//when
		List<FoodResponseDto> result = foodService.findAllFood("주인", 1L);

		//then
		assertThat(result.get(0).getId()).isEqualTo(foodId1);
		assertThat(result.get(1).getId()).isEqualTo(foodId2);
		assertThat(result.size()).isEqualTo(foodList.size());
	}

	@Test
	void 음식_NotFound_수정_실패() {
		//given
		Long foodId = 1L;
		String updateName = "음식수정";
		int updatePrice = 500;

		FoodRequestDto requestDto = new FoodRequestDto("음식이름", 1000);

		given(foodRepository.findById(foodId)).willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> foodService.updateFood(foodId, requestDto))
			.isInstanceOf(NotFoundException.class);

	}

}
