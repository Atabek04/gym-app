package com.epam.gym.controller;

import com.epam.gym.dto.BasicTrainerResponse;
import com.epam.gym.dto.TraineeRequest;
import com.epam.gym.dto.TraineeResponse;
import com.epam.gym.dto.TraineeTrainingFilterRequest;
import com.epam.gym.dto.TraineeUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserStatusRequest;
import com.epam.gym.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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
@Secured("Trainee")
@Tag(name = "Trainee")
public class TraineeController {

    private final TraineeService traineeService;

    @Operation(summary = "Create a new trainee", description = "Adds a new trainee to the system.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    public UserCredentials createTrainee(@Valid @RequestBody TraineeRequest request) {
        return traineeService.create(request);
    }

    @Operation(summary = "Get trainee by username", description = "Retrieves a trainee and their assigned trainers by username.")
    @GetMapping("/{username}")
    public TraineeResponse getTraineeByUsername(@Size(min = 2) @PathVariable String username) {
        return traineeService.getTraineeAndTrainers(username);
    }

    @Operation(summary = "Update trainee information", description = "Updates the details of an existing trainee.")
    @PutMapping("/{username}")
    public TraineeResponse updateTrainee(
            @Size(min = 2) @PathVariable String username,
            @Valid @RequestBody TraineeUpdateRequest request
    ) {
        return traineeService.updateTraineeAndUser(request, username);
    }

    @Operation(summary = "Update assigned trainers", description = "Updates the trainers assigned to a trainee.")
    @PutMapping("/{username}/trainers")
    public TraineeResponse updateTrainers(
            @Size(min = 2) @PathVariable String username,
            @RequestBody List<String> trainerUsernames) {
        traineeService.updateTrainers(username, trainerUsernames);
        return traineeService.getTraineeAndTrainers(username);
    }

    @Operation(summary = "Get not assigned trainers", description = "Retrieves trainers that are not assigned to the specified trainee.")
    @GetMapping("/{username}/trainers")
    public List<BasicTrainerResponse> getNotAssignedTrainers(@Size(min = 2) @PathVariable String username) {
        return traineeService.getNotAssignedTrainers(username);
    }

    @Operation(summary = "Get trainee trainings", description = "Retrieves all training sessions assigned to a specific trainee.")
    @GetMapping("/{username}/trainings")
    public List<TrainingResponse> getTraineeTrainings(
            @Size(min = 2) @PathVariable String username,
            @Valid @RequestBody TraineeTrainingFilterRequest filterRequest) {
        return traineeService.getTraineeTrainings(username, filterRequest);
    }

    @Operation(summary = "Update trainee status", description = "Updates the active status of the specified trainee.")
    @PatchMapping("/{username}/status")
    public void updateTraineeStatus(
            @PathVariable("username") String username,
            @RequestBody @Valid UserStatusRequest traineeStatusRequest
    ) {
        traineeService.updateTraineeStatus(username, traineeStatusRequest.isActive());
    }

    @Operation(summary = "Delete trainee", description = "Removes a trainee from the system.")
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainee(@PathVariable String username) {
        traineeService.delete(username);
    }
}