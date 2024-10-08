package com.epam.gym.dto;

import com.epam.gym.model.TrainingType;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record TrainingResponse(
        Long id,
        Long traineeId,
        Long trainerId,
        String traineeFirstName,
        String traineeLastName,
        String trainerFirstName,
        String trainerLastName,
        String trainingName,
        TrainingType trainingType,
        ZonedDateTime trainingDate,
        Long trainingDuration
) {
}
