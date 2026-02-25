package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends ApplicationException {

    public UnauthorizedAccessException(String message, HttpStatus status) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
