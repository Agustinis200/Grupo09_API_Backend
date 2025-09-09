package com.uade.tpo.marketplace.exception.cart;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
@ControllerAdvice
public class CartExceptionHandler {
    @ExceptionHandler(ItemNotFoundInCartException.class)
    public ResponseEntity<Object> handleItemNotFoundInCart(ItemNotFoundInCartException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Artículo no encontrado en el carrito");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<Object> handleInvalidQuantity(InvalidQuantityException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Cantidad no válida");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Object> handleCartNotFound(CartNotFoundException ex) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Carrito no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
