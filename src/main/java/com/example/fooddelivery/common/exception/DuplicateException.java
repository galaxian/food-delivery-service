package com.example.fooddelivery.common.exception;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {
	private final ErrorCode errorCode;

	public DuplicateException(String message) {
		super(message);
		this.errorCode = ErrorCode.DUPLICATE;
	}
}
