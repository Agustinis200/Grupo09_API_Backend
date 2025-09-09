package com.uade.tpo.marketplace.exception.order;

public class CartIsEmptyException extends RuntimeException {
    public CartIsEmptyException(String message) {
        super(message);
    }
    
}
