package com.epam.gym.controller;

import com.epam.gym.dto.TrainingRequest;
import com.epam.gym.dto.TrainingTypeResponse;
import com.epam.gym.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/trainings")
@Slf4j
@Secured({"ROLE_TRAINER", "ROLE_TRAINEE"})
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTraining(@Valid @RequestBody TrainingRequest request) {
        trainingService.create(request);
    }

    @GetMapping
    public List<TrainingTypeResponse> listAllTrainingTypes() {
        return trainingService.getAllTrainingTypes();
    }
}

