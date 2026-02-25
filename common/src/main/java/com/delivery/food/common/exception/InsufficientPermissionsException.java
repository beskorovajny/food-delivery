package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class InsufficientPermissionsException extends ApplicationException {

    public InsufficientPermissionsException(String message, HttpStatus status) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
