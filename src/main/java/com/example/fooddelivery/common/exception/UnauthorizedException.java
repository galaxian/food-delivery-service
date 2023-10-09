package com.example.fooddelivery.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{
	private final ErrorCode errorCode;

	public UnauthorizedException(String message) {
		super(message);
		this.errorCode = ErrorCode.UNAUTHORIZED;
	}
}
