package com.epam.gym.main.dto;

import com.epam.gym.main.model.TrainingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record
TrainingResponse(
        Long id,
        Long traineeId,
        Long trainerId,
        String traineeFirstName,
        String traineeLastName,
        String trainerFirstName,
        String trainerLastName,
        String trainingName,
        TrainingType trainingType,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime trainingDate,
        Long trainingDuration
) {
}
