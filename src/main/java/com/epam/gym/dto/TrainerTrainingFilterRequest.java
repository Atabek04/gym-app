package com.epam.gym.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
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
public class TrainerTrainingFilterRequest {

    @PastOrPresent(message = "Start date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodFrom;

    @PastOrPresent(message = "End date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodTo;

    private String traineeName;
}
