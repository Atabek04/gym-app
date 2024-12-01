package com.epam.gym.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TrainerResponse(
        String firstName,
        String lastName,
        String username,
        boolean isActive,
        String specialization,
        List<BasicTraineeResponse> trainees
) {
}
