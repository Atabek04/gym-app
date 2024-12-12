package com.epam.gym.trainingreport.dto;

import com.epam.gym.trainingreport.model.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadRequest implements Serializable {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private LocalDate trainingDate;
    private Integer trainingDuration;
    private ActionType actionType; // ADD or DELETE
}
