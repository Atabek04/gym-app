package com.epam.gym.trainingreport.dto;

import com.epam.gym.trainingreport.model.ActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainerWorkloadRequest(
        @Schema(example = "Super.Trainer")
        String username,

        @Schema(example = "Super")
        String firstName,

        @Schema(example = "Trainer")
        String lastName,

        @Schema(example = "true")
        Boolean isActive,

        @Schema(example = "2024-01-01")
        LocalDate trainingDate,

        @Schema(example = "60")
        Integer trainingDuration,

        @Schema(example = "ADD")
        ActionType actionType
) {
}
