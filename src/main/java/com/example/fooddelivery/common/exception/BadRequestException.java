package com.example.fooddelivery.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
	private final ErrorCode errorCode;

	public BadRequestException(String message) {
		super(message);
		this.errorCode = ErrorCode.BAD_REQUEST;
	}
}
