package com.example.fooddelivery.order.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAllOrderByPhoneReqDto {
	@NotNull(message = "전화번호를 입력해주세요")
	private String phoneNumber;

	public GetAllOrderByPhoneReqDto(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
