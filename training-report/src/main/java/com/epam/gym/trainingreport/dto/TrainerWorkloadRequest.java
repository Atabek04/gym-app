package com.epam.gym.trainingreport.dto;

import com.epam.gym.trainingreport.model.ActionType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @NotNull(message = "Training date is required")
    @FutureOrPresent(message = "Training date can't be in the past")
    private LocalDate trainingDate;

    @NotNull(message = "Training duration is required")
    @Positive( message = "Training duration should be greater than 0")
    private Integer trainingDuration;

    @NotNull(message = "Action type is required")
    private ActionType actionType;
}
