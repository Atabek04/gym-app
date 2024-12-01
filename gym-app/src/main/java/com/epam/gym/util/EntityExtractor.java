package com.epam.gym.util;

import com.epam.gym.exception.ResourceNotFoundException;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;

import java.util.Optional;

public class EntityExtractor {
    public <T> T findOrThrow(Optional<T> entity, String entityType, String identifier) {
        return entity.orElseThrow(() -> new ResourceNotFoundException(
                String.format("%s not found with identifier: %s", entityType, identifier)));
    }

    public Trainer findTrainerOrThrow(Optional<Trainer> trainerOptional, String traineeUsername) {
        return trainerOptional.orElseThrow(() -> new ResourceNotFoundException(
                String.format("Trainer not found for trainee with username: %s", traineeUsername)));
    }

    public Trainee findTraineeOrThrow(Optional<Trainee> traineeOptional, String traineeUsername) {
        return traineeOptional.orElseThrow(() -> new ResourceNotFoundException(
                String.format("Trainer not found for trainee with username: %s", traineeUsername)));
    }
}
