package com.epam.gym.main.controller.openapi;

import com.epam.gym.main.dto.BasicTrainerResponse;
import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.TraineeResponse;
import com.epam.gym.main.dto.TraineeUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.model.TrainingType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Trainee")
@SuppressWarnings("unused")
public interface TraineeApi {

    @Operation(summary = "Create a new trainee", description = "Adds a new trainee to the system.")
    UserCredentials createTrainee(@Valid @RequestBody TraineeRequest request);

    @Operation(summary = "Get trainee by username",
            description = "Retrieves a trainee and their assigned trainers by username.")
    TraineeResponse getTraineeByUsername(@NotBlank @PathVariable("username") @Parameter(example = "Abu.Yusuf") String username);

    @Operation(summary = "Update trainee information", description = "Updates the details of an existing trainee.")
    TraineeResponse updateTrainee(
            @NotBlank @PathVariable("username") @Parameter(example = "Abu.Yusuf") String username,
            @Valid @RequestBody TraineeUpdateRequest request
    );

    @Operation(summary = "Update assigned trainers", description = "Updates the trainers assigned to a trainee.")
    TraineeResponse updateTrainers(
            @NotBlank @PathVariable("username") @Parameter(example = "Abu.Yusuf") String username,
            @RequestBody List<String> trainerUsernames);

    @Operation(summary = "Get not assigned trainers",
            description = "Retrieves trainers that are not assigned to the specified trainee.")
    List<BasicTrainerResponse> getNotAssignedTrainers(@PathVariable("username") @Parameter(example = "Abu.Yusuf") String username);

    @Operation(summary = "Get trainee trainings",
            description = "Retrieves all training sessions assigned to a specific trainee.")
    List<TrainingResponse> getTraineeTrainings(
            @NotBlank
            @PathVariable("username")
            @Parameter(example = "Super.Trainee")
            String username,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(example = "2024-01-01")
            LocalDateTime periodFrom,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(example = "2025-01-01")
            LocalDateTime periodTo,

            @RequestParam(required = false)
            @Parameter(example = "Super.Trainer")
            String trainerName,

            @RequestParam(required = false)
            @Parameter(example = "CARDIO")
            TrainingType trainingType
    );


    @Operation(summary = "Delete trainee", description = "Removes a trainee from the system.")
    void deleteTrainee(@NotBlank @PathVariable("username") @Parameter(example = "Abu.Yusuf") String username);
}