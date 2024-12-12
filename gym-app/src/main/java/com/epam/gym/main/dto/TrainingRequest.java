package com.epam.gym.main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TrainingRequest(
        @NotBlank(message = "Trainee username is required")
        @Schema(example = "Super.Trainee")
        String traineeUsername,

        @NotBlank(message = "Trainer username is required")
        @Schema(example = "Super.Trainer")
        String trainerUsername,

        @NotBlank(message = "Training name is required")
        @Schema(example = "Super Cool Training Name")
        String trainingName,

        @NotNull(message = "Training date is required")
        @FutureOrPresent(message = "Training date cannot be in the past")
        @Schema(example = "2025-01-01T14:00:00")
        LocalDateTime trainingDate,

        @NotNull(message = "Training duration is required")
        @Positive(message = "Training duration cannot be negative number")
        @Schema(example = "120")
        Long trainingDuration
) {
}
