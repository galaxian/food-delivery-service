package com.example.fooddelivery.auth.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginReqDto {

	@NotNull(message = "로그인할 아이디를 입력해주세요")
	private String identifier;

	@NotNull(message = "비밀번호를 입력해주세요")
	private String password;

	public LoginReqDto(String identifier, String password) {
		this.identifier = identifier;
		this.password = password;
	}
}
