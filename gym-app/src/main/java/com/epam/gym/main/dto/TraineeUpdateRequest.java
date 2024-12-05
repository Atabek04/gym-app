package com.epam.gym.main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TraineeUpdateRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 2, message = "First name must have at least 2 characters")
        @Schema(example = "Abu")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, message = "Last name must have at least 2 characters")
        @Schema(example = "Yusuf")
        String lastName,

        @Schema(example = "Bagdad 101")
        String address,

        @Past(message = "Date of birth must be in the past")
        @Schema(example = "729-12-01")
        LocalDate dateOfBirth,

        @NotNull(message = "isActive is required")
        @Schema(example = "false")
        Boolean isActive
) {
}