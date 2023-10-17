package com.example.fooddelivery.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode(), e.getMessage()),
                HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode(), e.getMessage()),
            HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode(), e.getMessage()),
            HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode(), e.getMessage()),
            HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }
}
