package com.epam.gym.trainingreport.controller;

import com.epam.gym.trainingreport.controller.openapi.TrainerWorkloadApi;
import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.dto.TrainerWorkloadResponse;
import com.epam.gym.trainingreport.exception.TrainerWorkloadNotFoundException;
import com.epam.gym.trainingreport.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> handleTrainerWorkloadNotFoundException(TrainerWorkloadNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
