package com.uade.tpo.marketplace.exception.category;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CategoryExceptionHandler {
    @ExceptionHandler(CategoryDuplicateException.class)
    public ResponseEntity<Object> handleCategoryDuplicateException(CategoryDuplicateException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "La categoría que se intenta agregar ya existe");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Categoría no encontrada");
        return ResponseEntity.status(404).body(body);
    }
}