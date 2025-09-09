package com.uade.tpo.marketplace.exception.cart;

public class ItemNotFoundInCartException extends RuntimeException {
    public ItemNotFoundInCartException(String message) {
        super(message);
    }
    
}
