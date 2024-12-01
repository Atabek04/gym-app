package com.epam.gym.dto;

import com.epam.gym.validation.annotation.ValidSpecialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TrainerRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 2)
        @Schema(example = "Super")
        String firstName,

        @Size(min = 2)
        @NotBlank(message = "Last name is required")
        @Schema(example = "Trainer")
        String lastName,

        @NotBlank(message = "Specialization is required")
        @ValidSpecialization
        @Schema(example = "CARDIO")
        String specialization
) {
}
