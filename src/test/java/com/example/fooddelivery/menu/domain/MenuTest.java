package com.example.fooddelivery.menu.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.fooddelivery.restaurant.domain.Restaurant;

class MenuTest {

	private static final Restaurant restaurant = new Restaurant(1L, "치킨집", 10000,
		3000, null, null, null);

	@ParameterizedTest
	@ValueSource(ints = {0, 10, 200, 231231})
	void 메뉴_가격이_음수가_아니면_생성_성공(int price) {
		//given
		//when
		//then
		assertDoesNotThrow(() -> Menu.createMenu("양념치킨", price, "비법소스로만든치킨", restaurant));
	}

}
