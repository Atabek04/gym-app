package com.epam.gym.authservice.exception;

public class JWTException extends RuntimeException {

    public JWTException(String message) {
        super(message);
    }
}
