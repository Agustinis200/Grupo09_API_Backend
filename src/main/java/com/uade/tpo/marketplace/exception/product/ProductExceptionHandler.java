package com.uade.tpo.marketplace.exception.product;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Producto no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ProductDuplicateException.class)
    public ResponseEntity<Object> handleProductDuplicate(ProductDuplicateException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "El producto que se intenta agregar ya existe");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStock(InsufficientStockException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "No hay stock suficiente para completar la operación");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ProductDeletionException.class)
    public ResponseEntity<Object> handleProductDeletion(ProductDeletionException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 409);
        body.put("error", "Conflict");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "No se puede eliminar el producto debido a restricciones de integridad");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
