package com.example.fooddelivery.menu.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.fooddelivery.restaurant.domain.Address;
import com.example.fooddelivery.restaurant.domain.City;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.domain.State;

class MenuTest {

	private static final Restaurant restaurant = new Restaurant(1L, "치킨집", 10000,
		3000,
		new Address("서울특별시", "서초구", "선릉", "기타주소"),
		null, null, null, null);

	@ParameterizedTest
	@ValueSource(ints = {0, 10, 200, 231231})
	void 메뉴_가격이_음수가_아니면_생성_성공(int price) {
		//given
		//when
		//then
		assertDoesNotThrow(() -> Menu.createMenu("양념치킨", price, "비법소스로만든치킨", restaurant));
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, -10, -1231231})
	void 메뉴의_가격이_음수이면_생성_실패(int price) {
		//given
		//when
		//then
		assertThatThrownBy(() -> Menu.createMenu("양념치킨", price, "비법소스로만든치킨",restaurant))
			.isInstanceOf(IllegalArgumentException.class);

	}

}
