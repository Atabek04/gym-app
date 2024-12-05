package com.epam.gym.trainingreport.dto;

import com.epam.gym.trainingreport.model.TrainingYear;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TrainerWorkloadResponse(
        String username,
        @JsonProperty("years")
        List<TrainingYear> yearlySummary
) {
}
