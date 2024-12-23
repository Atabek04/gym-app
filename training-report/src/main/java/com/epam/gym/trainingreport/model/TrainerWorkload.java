package com.epam.gym.trainingreport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "trainer_workloads")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerWorkload {
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;

    @Field("yearly_summary")
    private List<TrainingYear> yearlySummary = new ArrayList<>();
}
