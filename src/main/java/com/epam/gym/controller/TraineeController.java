package com.epam.gym.controller;


import com.epam.gym.dto.BasicTrainerResponse;
import com.epam.gym.dto.TraineeRequest;
import com.epam.gym.dto.TraineeResponse;
import com.epam.gym.dto.TraineeTrainingFilterRequest;
import com.epam.gym.dto.TraineeUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserStatusRequest;
import com.epam.gym.security.Secured;
import com.epam.gym.security.UserRole;
import com.epam.gym.service.TraineeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/trainees")
@RequiredArgsConstructor
@Slf4j
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserCredentials createTrainee(@Valid @RequestBody TraineeRequest request) {
        return traineeService.create(request);
    }

    @Secured({UserRole.ROLE_TRAINEE})
    @GetMapping("/{username}")
    public TraineeResponse getTraineeByUsername(@Size(min = 2) @PathVariable String username) {
        return traineeService.getTraineeAndTrainers(username);
    }

    @Secured({UserRole.ROLE_TRAINEE})
    @PutMapping("/{username}")
    public TraineeResponse updateTrainee(
            @Size(min = 2) @PathVariable String username,
            @Valid @RequestBody TraineeUpdateRequest request
    ) {
        return traineeService.updateTraineeAndUser(request, username);
    }

    @Secured({UserRole.ROLE_TRAINEE})
    @PutMapping("/{username}/trainers")
    public TraineeResponse updateTrainers(
            @Size(min = 2) @PathVariable String username,
            @RequestBody List<String> trainerUsernames) {
        traineeService.updateTrainers(username, trainerUsernames);
        return traineeService.getTraineeAndTrainers(username);
    }

    @Secured({UserRole.ROLE_TRAINEE})
    @GetMapping("/{username}/trainers")
    public List<BasicTrainerResponse> getNotAssignedTrainers(@Size(min = 2) @PathVariable String username) {
        return traineeService.getNotAssignedTrainers(username);
    }

    @Secured({UserRole.ROLE_TRAINEE})
    @GetMapping("/{username}/trainings")
    public List<TrainingResponse> getTraineeTrainings(
            @Size(min = 2) @PathVariable String username,
            @Valid @RequestBody TraineeTrainingFilterRequest filterRequest) {
        return traineeService.getTraineeTrainings(username, filterRequest);
    }

    @Secured({UserRole.ROLE_TRAINEE})
    @PatchMapping("/{username}/status")
    public void updateTraineeStatus(
            @PathVariable("username") String username,
            @RequestBody @Valid UserStatusRequest traineeStatusRequest
    ) {
        traineeService.updateTraineeStatus(username, traineeStatusRequest.isActive());
    }

    @Secured({UserRole.ROLE_TRAINEE})
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainee(@PathVariable String username) {
        traineeService.delete(username);
    }
}
