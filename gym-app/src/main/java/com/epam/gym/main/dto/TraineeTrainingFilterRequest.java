package com.epam.gym.main.dto;

import com.epam.gym.main.model.TrainingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingFilterRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime periodFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime periodTo;

    private String trainerName;

    private TrainingType trainingType;
}
