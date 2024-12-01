package com.epam.gym.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum TrainingType {
    CARDIO(1),
    STRENGTH_TRAINING(2),
    YOGA(3),
    PILATES(4),
    CARDIO_TRAINING(5),
    HIIT(6),
    FUNCTIONAL_FITNESS(7),
    GROUP_FITNESS(8),
    MARTIAL_ARTS(9),
    CIRCUIT_TRAINING(10),
    OUTDOOR_FITNESS(11),
    MOBILITY_TRAINING(12),
    CROSSFIT(13);

    private final Integer id;

    public static TrainingType fromId(Integer id) {
        for (TrainingType type : TrainingType.values()) {
            if (Objects.equals(type.getId(), id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TrainingType ID: " + id);
    }

}