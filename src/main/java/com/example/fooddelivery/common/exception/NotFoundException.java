package com.example.fooddelivery.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public NotFoundException(String message) {
        super(message);
        this.errorCode = ErrorCode.NOT_FOUND;
    }
}
