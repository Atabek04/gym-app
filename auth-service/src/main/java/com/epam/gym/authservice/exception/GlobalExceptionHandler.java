package com.epam.gym.authservice.exception;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.ws.rs.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication error: {}", ex.getMessage());
        return new ResponseEntity<>("Authentication failed: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Fetching error: {}", ex.getMessage());
        return new ResponseEntity<>("Fetching failed: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException ex) {
        log.error("Malformed JWT: {}", ex.getMessage());
        return new ResponseEntity<>("Invalid JWT: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return new ResponseEntity<>("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("Undefined exception: {}", ex.getMessage());
        return new ResponseEntity<>("Undefined exception happened: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
