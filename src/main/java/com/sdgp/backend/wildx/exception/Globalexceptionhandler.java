package com.sdgp.backend.wildx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handler for the WildX project.
 * It ensures that the Flutter frontend always receives a consistent 
 * JSON error structure, making debugging and UI alerts much easier.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles 404 errors when a requested resource (Animal or Photo) isn't found.
     * Maps to a standard Not Found response.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles 400 Bad Request errors, specifically for @Valid failures.
     * Extracts field-specific errors so the mobile app can highlight exactly what went wrong.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        
        // Extracting each validation failure (e.g., "title cannot be empty")
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 400);
        body.put("error", "Validation Failed");
        body.put("details", fieldErrors); // Returns specific fields that failed validation
        
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Global "Safety Net" for any unexpected server-side exceptions (500 errors).
     * Prevents the backend from leaking sensitive stack traces to the frontend.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, 
                "An unexpected system error occurred: " + ex.getMessage());
    }

    /**
     * Utility method to keep the error JSON format uniform across the entire API.
     */
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        
        return ResponseEntity.status(status).body(body);
    }
}