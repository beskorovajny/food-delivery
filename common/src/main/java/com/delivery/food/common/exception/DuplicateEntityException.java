package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends ApplicationException {

    public DuplicateEntityException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
