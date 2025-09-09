package com.uade.tpo.marketplace.exception.user;

public class UserMailDuplicateException extends RuntimeException {
    public UserMailDuplicateException(String message) {
        super(message);
    }
}
