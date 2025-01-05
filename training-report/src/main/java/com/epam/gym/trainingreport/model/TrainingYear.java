package com.epam.gym.trainingreport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingYear {
    @JsonIgnore
    private Long id;
    private Integer year;

    @Field("training_durations")
    private List<TrainingDuration> trainingDurations = new ArrayList<>();
}