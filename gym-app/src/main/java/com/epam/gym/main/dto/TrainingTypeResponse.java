package com.epam.gym.main.dto;

import com.epam.gym.main.model.TrainingType;
import lombok.Builder;

@Builder
public record TrainingTypeResponse(
        Integer trainingTypeId,
        TrainingType trainingType
) {
}
