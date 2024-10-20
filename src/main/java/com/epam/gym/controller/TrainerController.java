package com.epam.gym.controller;

import com.epam.gym.dto.TrainerRequest;
import com.epam.gym.dto.TrainerResponse;
import com.epam.gym.dto.TrainerTrainingFilterRequest;
import com.epam.gym.dto.TrainerUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserStatusRequest;
import com.epam.gym.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
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
@RequestMapping("v1/trainers")
@RequiredArgsConstructor
@Slf4j
@Secured("ROLE_TRAINER")
@Tag(name = "Trainer")
public class TrainerController {

    private final TrainerService trainerService;

    @Operation(summary = "Create a new trainer", description = "Adds a new trainer to the system.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    public UserCredentials createTrainer(@Valid @RequestBody TrainerRequest request) {
        return trainerService.create(request);
    }

    @Operation(summary = "Get trainer by username", description = "Retrieves a trainer and their assigned trainees by username.")
    @GetMapping("/{username}")
    public TrainerResponse getTrainerByUsername(@PathVariable("username") String username) {
        return trainerService.getTrainerAndTrainees(username);
    }

    @Operation(summary = "Update trainer information", description = "Updates the details of an existing trainer.")
    @PutMapping("/{username}")
    public TrainerResponse updateTrainer(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerUpdateRequest request
    ) {
        return trainerService.updateTrainerAndUser(request, username);
    }

    @Operation(summary = "Delete a trainer", description = "Removes a trainer from the system.")
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainer(@PathVariable("username") String username) {
        trainerService.delete(username);
    }

    @Operation(summary = "Get trainings for a trainer", description = "Retrieves all training sessions assigned to a specific trainer.")
    @GetMapping("/{username}/trainings")
    public List<TrainingResponse> getTrainerTrainings(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerTrainingFilterRequest filterRequest
    ) {
        return trainerService.findTrainerTrainings(username, filterRequest);
    }

    @Operation(summary = "Update trainer status", description = "Updates the active status of the specified trainer.")
    @PatchMapping("/{username}/status")
    public void updateTrainerStatus(
            @PathVariable("username") String username,
            @RequestBody @Valid UserStatusRequest trainerStatusRequest
    ) {
        trainerService.updateTrainerStatus(username, trainerStatusRequest.isActive());
    }
}
