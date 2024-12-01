package com.epam.gym.dto;

import com.epam.gym.model.TrainingType;
import lombok.Builder;

@Builder
public record TrainingTypeResponse(
        Integer trainingTypeId,
        TrainingType trainingType
) {
}
