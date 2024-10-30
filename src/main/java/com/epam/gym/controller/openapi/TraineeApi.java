package com.epam.gym.controller.openapi;

import com.epam.gym.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Trainee")
public interface TraineeApi {

    @Operation(summary = "Create a new trainee", description = "Adds a new trainee to the system.")
    UserCredentials createTrainee(@RequestBody TraineeRequest request);

    @Operation(summary = "Get trainee by username", description = "Retrieves a trainee and their assigned trainers by username.")
    TraineeResponse getTraineeByUsername(@PathVariable String username);

    @Operation(summary = "Update trainee information", description = "Updates the details of an existing trainee.")
    TraineeResponse updateTrainee(@PathVariable String username, @RequestBody TraineeUpdateRequest request);

    @Operation(summary = "Update assigned trainers", description = "Updates the trainers assigned to a trainee.")
    TraineeResponse updateTrainers(@PathVariable String username, @RequestBody List<String> trainerUsernames);

    @Operation(summary = "Get not assigned trainers", description = "Retrieves trainers that are not assigned to the specified trainee.")
    List<BasicTrainerResponse> getNotAssignedTrainers(@PathVariable String username);

    @Operation(summary = "Get trainee trainings", description = "Retrieves all training sessions assigned to a specific trainee.")
    List<TrainingResponse> getTraineeTrainings(@PathVariable String username, @RequestBody TraineeTrainingFilterRequest filterRequest);

    @Operation(summary = "Update trainee status", description = "Updates the active status of the specified trainee.")
    void updateTraineeStatus(@PathVariable String username, @RequestBody UserStatusRequest traineeStatusRequest);

    @Operation(summary = "Delete trainee", description = "Removes a trainee from the system.")
    void deleteTrainee(@PathVariable String username);
}