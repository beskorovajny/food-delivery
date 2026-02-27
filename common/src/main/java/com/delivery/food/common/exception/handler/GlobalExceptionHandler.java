package com.delivery.food.common.exception.handler;

import com.delivery.food.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for all services.
 *
 * <p>Centralizes error handling and returns consistent JSON responses.
 * All business and validation exceptions are mapped to appropriate HTTP statuses.
 * Unexpected exceptions are logged as ERROR and return 500.
 * </p>
 *
 * <p>Response format:</p>
 * <pre>
 * {
 *   "timestamp": "2026-02-25T14:30:00Z",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Restaurant not found with id: 123",
 *   "details": null or ["field: message", ...]
 * }
 * </pre>
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {private static ErrorResponse buildResponse(
        String message, HttpStatus status, List<String> details) {
    return new ErrorResponse(
            Instant.now().toString(),
            status.value(),
            status.getReasonPhrase(),
            message,
            details
    );
}

    // ──────────────────────────────────────────────
    // Business / expected exceptions (WARN level)
    // ──────────────────────────────────────────────

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null));
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntity(DuplicateEntityException ex) {
        log.warn("Duplicate entity: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildResponse(ex.getMessage(), HttpStatus.CONFLICT, null));
    }

    @ExceptionHandler(InsufficientPermissionsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPermissions(InsufficientPermissionsException ex) {
        log.warn("Insufficient permissions: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN, null));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedAccessException ex) {
        log.warn("Unauthorized access: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, null));
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperation(InvalidOperationException ex) {
        log.warn("Invalid operation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null));
    }

    @ExceptionHandler(InventoryUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleInventoryUnavailable(InventoryUnavailableException ex) {
        log.warn("Inventory unavailable: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildResponse(ex.getMessage(), HttpStatus.CONFLICT, null));
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ErrorResponse> handlePaymentFailed(PaymentFailedException ex) {
        log.warn("Payment failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(buildResponse(ex.getMessage(), HttpStatus.PAYMENT_REQUIRED, null));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceeded(RateLimitExceededException ex) {
        log.warn("Rate limit exceeded: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(buildResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS, null));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex) {
        log.warn("Service unavailable: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildResponse(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE, null));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null));
    }

    // ──────────────────────────────────────────────
    // Spring validation errors (400)
    // ──────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn("Validation failed: {}", details);
        return ResponseEntity.badRequest()
                .body(buildResponse("Validation failed", HttpStatus.BAD_REQUEST, details));
    }

    // ──────────────────────────────────────────────
    // Catch-all for unexpected errors (500)
    // ──────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return ResponseEntity.internalServerError()
                .body(buildResponse("Internal server error. Please try again later.",
                        HttpStatus.INTERNAL_SERVER_ERROR, null));
    }
}
