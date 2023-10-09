package com.example.fooddelivery.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResDto {
	private String accessToken;

	public LoginResDto(String accessToken) {
		this.accessToken = accessToken;
	}
}
