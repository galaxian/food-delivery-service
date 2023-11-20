package com.example.fooddelivery.owner.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnerJoinReqDto {

	private static final String IDENTIFIER_REGEX = "^[a-z0-9]+$";
	private static final int IDENTIFIER_MIN_SIZE = 6;
	private static final int IDENTIFIER_MAX_SIZE = 12;

	private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*\\d)[a-z\\d!@#\\$%\\^&\\*\\(\\)~]+$";
	private static final int PASSWORD_MIN_SIZE = 8;
	private static final int PASSWORD_MAX_SIZE = 15;

	@NotNull(message = "아이디를 입력해주세요")
	@Size(min = IDENTIFIER_MIN_SIZE, max = IDENTIFIER_MAX_SIZE,
		message = "6~12자 사이의 아이디를 입력해주세요")
	@Pattern(regexp = IDENTIFIER_REGEX, message = "영문자와 숫자를 포함한 아이디를 입력해주세요")
	private String identifier;

	@NotNull(message = "비밀번호를 입력해주세요")
	@Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE,
		message = "8~15자 사이의 비밀번호를 입력해주세요")
	@Pattern(regexp = PASSWORD_REGEX, message = "영문자와 숫자, 특수문자를 포함한 비밀번호를 입력해주세요")
	private String password;

	public OwnerJoinReqDto(String identifier, String password) {
		this.identifier = identifier;
		this.password = password;
	}
}
