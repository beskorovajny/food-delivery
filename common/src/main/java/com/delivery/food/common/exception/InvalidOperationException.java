package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidOperationException extends ApplicationException {

    public InvalidOperationException(String message, HttpStatus status) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
