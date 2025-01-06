package com.epam.gym.trainingreport.controller;

import com.epam.gym.trainingreport.controller.openapi.TrainerWorkloadApi;
import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.dto.TrainerWorkloadResponse;
import com.epam.gym.trainingreport.exception.TrainerWorkloadNotFoundException;
import com.epam.gym.trainingreport.service.TrainerWorkloadService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/workload")
public class TrainerWorkloadController implements TrainerWorkloadApi {

    private final TrainerWorkloadService service;

    @PostMapping("/report")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTrainerWorkload(@Valid @RequestBody TrainerWorkloadRequest request) {
        log.info("Received request for adding workload");
        service.createTrainerWorkload(request);
    }

    @GetMapping("/{username}")
    public TrainerWorkloadResponse getTrainerWorkload(@PathVariable("username") String username) {
        log.info("Received request for getting trainer summary");
        return service.getTrainerWorkload(username);
    }

    @ExceptionHandler(TrainerWorkloadNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTrainerWorkloadNotFoundException(TrainerWorkloadNotFoundException ex) {
        Map<String, String> errorResponse = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation failed: {}", ex.getMessage());
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Validation error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}