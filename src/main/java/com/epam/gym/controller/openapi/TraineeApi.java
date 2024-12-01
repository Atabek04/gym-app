package com.epam.gym.controller.openapi;

import com.epam.gym.dto.BasicTrainerResponse;
import com.epam.gym.dto.TraineeRequest;
import com.epam.gym.dto.TraineeResponse;
import com.epam.gym.dto.TraineeUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserStatusRequest;
import com.epam.gym.model.TrainingType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Trainee")
public interface TraineeApi {

    @Operation(summary = "Create a new trainee", description = "Adds a new trainee to the system.")
    UserCredentials createTrainee(TraineeRequest request);

    @Operation(summary = "Get trainee by username",
            description = "Retrieves a trainee and their assigned trainers by username.")
    TraineeResponse getTraineeByUsername(@Parameter(example = "Abu.Yusuf") String username);

    @Operation(summary = "Update trainee information", description = "Updates the details of an existing trainee.")
    TraineeResponse updateTrainee(
            @Parameter(example = "Abu.Yusuf") String username,
            TraineeUpdateRequest request
    );

    @Operation(summary = "Update assigned trainers", description = "Updates the trainers assigned to a trainee.")
    TraineeResponse updateTrainers(
            @Parameter(example = "Abu.Yusuf") String username,
            @Parameter(example = "[\"Abu.Hanifa\", \"Super.Trainer\"]") List<String> trainerUsernames);

    @Operation(summary = "Get not assigned trainers",
            description = "Retrieves trainers that are not assigned to the specified trainee.")
    List<BasicTrainerResponse> getNotAssignedTrainers(@Parameter(example = "Abu.Yusuf") String username);

    @Operation(summary = "Get trainee trainings",
            description = "Retrieves all training sessions assigned to a specific trainee.")
    List<TrainingResponse> getTraineeTrainings(
            @Parameter(example = "Super.Trainee") String username,
            @Parameter(example = "2024-01-01") LocalDateTime periodFrom,
            @Parameter(example = "2025-01-01") LocalDateTime periodTo,
            @Parameter(example = "Super.Trainer") String trainerName,
            @Parameter(example = "CARDIO") TrainingType trainingType);


    @Operation(summary = "Update trainee status", description = "Updates the active status of the specified trainee.")
    void updateTraineeStatus(
            @Parameter(example = "Abu.Yusuf") String username,
            UserStatusRequest traineeStatusRequest
    );

    @Operation(summary = "Delete trainee", description = "Removes a trainee from the system.")
    void deleteTrainee(@Parameter(example = "Abu.Yusuf") String username);
}