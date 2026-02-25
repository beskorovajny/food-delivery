package com.delivery.food.common.exception;

import org.springframework.http.HttpStatus;

public class PaymentFailedException extends ApplicationException {

    public PaymentFailedException(String message, HttpStatus status) {
        super(message, HttpStatus.PAYMENT_REQUIRED);
    }
}
