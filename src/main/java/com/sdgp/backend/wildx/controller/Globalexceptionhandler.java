package com.sdgp.backend.wildx.controller;

import com.sdgp.backend.wildx.exception.ResourceNotFoundException;
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
 * Ensures that the Flutter frontend always receives a consistent 
 * error structure, making debugging and user alerts much easier.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles 404 errors when a requested animal, photo, or park isn't found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles 400 Bad Request errors, specifically for validation failures.
     * If a user tries to share a photo without a title or URL, this catches it.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        
        // Extracting specific field errors to tell the user exactly what is missing
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 400);
        body.put("error", "Validation Failed");
        body.put("details", fieldErrors);
        
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Global "Safety Net" for any unexpected server-side exceptions (500 errors).
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