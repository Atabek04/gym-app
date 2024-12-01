package com.epam.gym.dto;

import lombok.Builder;

@Builder
public record BasicTrainerResponse(
        String firstName,
        String lastName,
        String username,
        boolean isActive,
        String specialization
) {
}
