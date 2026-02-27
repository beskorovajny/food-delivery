package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApplicationException {

    public ValidationException(String message, HttpStatus status) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
