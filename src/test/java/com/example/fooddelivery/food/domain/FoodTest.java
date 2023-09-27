package com.example.fooddelivery.food.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.fooddelivery.restaurant.domain.Restaurant;

class FoodTest {

	@ParameterizedTest
	@ValueSource(ints = {0, 10, 200, 231231})
	void 음식의_가격이_음수가_아니면_생성_성공(int price) {
		//given
		String name = "음식이름";
		Restaurant restaurant = new Restaurant(1L, "식당이름", 10000, 1000,
			null, null, null);

		//when
		//then
		assertDoesNotThrow(() -> Food.createFood(name, price, restaurant));

	}

	@ParameterizedTest
	@ValueSource(ints = {-1, -10, -1231231})
	void 음식의_가격이_음수이면_생성_실패(int price) {
		//given
		String name = "음식이름";
		Restaurant restaurant = new Restaurant(1L, "식당이름", 10000, 1000,
			null, null, null);

		//when
		//then
		assertThatThrownBy(() -> Food.createFood(name, price, restaurant))
			.isInstanceOf(IllegalArgumentException.class);

	}

}
