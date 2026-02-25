package com.delivery.food.common.exception.handler;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(String timestamp,
                            int status,
                            String error,
                            String message,
                            List<String> details) {
    public ErrorResponse(String message, int status, List<String> details) {
        this(Instant.now().toString(), status, HttpStatus.valueOf(status).getReasonPhrase(), message, details);
    }

    public ErrorResponse(String message, int status) {
        this(message, status, null);
    }
}
