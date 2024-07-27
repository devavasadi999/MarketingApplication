package com.example.marketingapplication.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex, WebRequest request) {
        Map<String, String> errorMap  = Map.of("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errorMap);
    }

    // Handle all other system exceptions
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleGlobalException(Throwable ex, WebRequest request) {
        Map<String, String> errorMap  = Map.of("message", ex.getMessage());
        return ResponseEntity.internalServerError().body(errorMap);
    }
}

