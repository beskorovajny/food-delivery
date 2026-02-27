package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ApplicationException {

    public ServiceUnavailableException(String message, HttpStatus status) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
