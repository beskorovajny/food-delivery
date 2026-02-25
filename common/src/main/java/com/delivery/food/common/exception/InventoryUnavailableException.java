package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class InventoryUnavailableException extends ApplicationException {

    public InventoryUnavailableException(String message, HttpStatus status) {
        super(message, HttpStatus.CONFLICT);
    }
}
