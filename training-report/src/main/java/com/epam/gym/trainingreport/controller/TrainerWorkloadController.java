package com.epam.gym.trainingreport.controller;

import com.epam.gym.trainingreport.controller.openapi.TrainerWorkloadApi;
import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.dto.TrainerWorkloadResponse;
import com.epam.gym.trainingreport.exception.TrainerWorkloadNotFoundException;
import com.epam.gym.trainingreport.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainings")
public class TrainerWorkloadController implements TrainerWorkloadApi {

    private final TrainerWorkloadService service;

    @PostMapping("/report")
    public void handleTraining(@RequestBody TrainerWorkloadRequest request) {
        service.processTraining(request);
    }

    @GetMapping("/{username}")
    public TrainerWorkloadResponse getTrainerSummary(@PathVariable String username) {
        return service.getTrainerSummary(username);
    }

    @ExceptionHandler(TrainerWorkloadNotFoundException.class)
    public ResponseEntity<String> handleTrainerWorkloadNotFoundException(TrainerWorkloadNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
