package com.example.fooddelivery.restaurant.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CityTest {

	@ParameterizedTest
	@ValueSource(strings = {"서초구", "강북구", "강남구"})
	@DisplayName("등록되어 있는 시군구 입력 시 생성 성공")
	void successGetEnumCity(String city) {
		//given
		//when
		//then
		assertDoesNotThrow(() -> City.getEnumCity(city));
	}

}
