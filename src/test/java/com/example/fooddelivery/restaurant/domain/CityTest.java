package com.example.fooddelivery.restaurant.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.fooddelivery.common.exception.BadRequestException;

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

	@ParameterizedTest
	@ValueSource(strings = {"경기도", "asc", "평양"})
	@DisplayName("등록되어 있지 않은 시군구 입력 시 생성 실패")
	void failGetEnumCity(String city) {
		//given
		//when
		//then
		assertThatThrownBy(() -> City.getEnumCity(city))
			.isInstanceOf(IndexOutOfBoundsException.class);
	}

	@Test
	@DisplayName("Null 입력 시 생성 실패")
	void failGetEnumCityNull() {
		//given
		//when
		//then
		assertThatThrownBy(() -> City.getEnumCity(null))
			.isInstanceOf(BadRequestException.class);
	}

}
