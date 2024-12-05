package com.epam.gym.main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserCredentials(
        @NotBlank(message = "Username is required")
        @Schema(example = "Super.Trainer")
        String username,

        @NotBlank(message = "Password is required")
        @Schema(example = "123")
        String password
) {
}
