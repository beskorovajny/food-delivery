package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class RateLimitExceededException extends ApplicationException {

    public RateLimitExceededException(String message, HttpStatus status) {
        super(message, HttpStatus.TOO_MANY_REQUESTS);
    }
}
