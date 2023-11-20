package com.example.fooddelivery.common.exception;

import static com.example.fooddelivery.common.exception.ErrorCode.*;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(BAD_REQUEST, message));
    }

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
