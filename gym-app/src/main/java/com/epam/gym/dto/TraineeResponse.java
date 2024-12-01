package com.epam.gym.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TraineeResponse(
        String username,
        String firstName,
        String lastName,
        String dateOfBirth,
        String address,
        boolean isActive,
        List<BasicTrainerResponse> trainers
) {
}