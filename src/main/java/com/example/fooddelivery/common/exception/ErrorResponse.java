package com.example.fooddelivery.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int status;
    private String message;
    private String code;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.status = errorCode.getStatus();
        this.message = message;
        this.code = errorCode.getErrorCode();
    }
}
