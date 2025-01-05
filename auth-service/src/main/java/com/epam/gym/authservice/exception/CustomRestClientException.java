package com.epam.gym.authservice.exception;

public class CustomRestClientException extends RuntimeException{
    public CustomRestClientException(String message) {
        super(message);
    }
}
