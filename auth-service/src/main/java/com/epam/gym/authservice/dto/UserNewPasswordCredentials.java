package com.epam.gym.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserNewPasswordCredentials(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Old Password is required")
        String oldPassword,
        @NotBlank(message = "New Password is required")
        String newPassword
) {
}
