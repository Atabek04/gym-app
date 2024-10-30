package com.epam.gym.controller.openapi;

import com.epam.gym.dto.TrainerRequest;
import com.epam.gym.dto.TrainerResponse;
import com.epam.gym.dto.TrainerTrainingFilterRequest;
import com.epam.gym.dto.TrainerUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Trainer")
public interface TrainerApi {

    @Operation(summary = "Create a new trainer", description = "Adds a new trainer to the system.")
    UserCredentials createTrainer(@Valid @RequestBody TrainerRequest request);

    @Operation(summary = "Get trainer by username", description = "Retrieves a trainer and their assigned trainees by username.")
    TrainerResponse getTrainerByUsername(@PathVariable("username") String username);

    @Operation(summary = "Update trainer information", description = "Updates the details of an existing trainer.")
    TrainerResponse updateTrainer(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerUpdateRequest request
    );

    @Operation(summary = "Delete a trainer", description = "Removes a trainer from the system.")
    void deleteTrainer(@PathVariable("username") String username);

    @Operation(summary = "Get trainings for a trainer", description = "Retrieves all training sessions assigned to a specific trainer.")
    List<TrainingResponse> getTrainerTrainings(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerTrainingFilterRequest filterRequest
    );

    @Operation(summary = "Update trainer status", description = "Updates the active status of the specified trainer.")
    void updateTrainerStatus(
            @PathVariable("username") String username,
            @RequestBody @Valid UserStatusRequest trainerStatusRequest
    );
}