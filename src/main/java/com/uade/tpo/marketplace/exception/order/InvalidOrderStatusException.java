package com.uade.tpo.marketplace.exception.order;

public  class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(String message) {
        super(message);
    }
    
}
