package com.example.fooddelivery.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "BAD_REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN"),
    NOT_FOUND(404, "NOT_FOUND"),
    DUPLICATE(409, "DUPLICATE"),
    INTERNAL_SERVER_ERROR(500, "COMMON_ERROR");

    private final int status;
    private final String errorCode;

    ErrorCode(int status, String errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }
}
