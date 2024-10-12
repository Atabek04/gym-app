package com.epam.gym.dto;

import com.epam.gym.model.TrainingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingFilterRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodTo;

    private String trainerName;

    private TrainingType trainingType;
}
