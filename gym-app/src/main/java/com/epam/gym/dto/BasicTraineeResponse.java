package com.epam.gym.dto;

import lombok.Builder;

@Builder
public record BasicTraineeResponse(
        String username,
        String firstName,
        String lastName,
        String dateOfBirth,
        String address,
        boolean isActive
) {
}
