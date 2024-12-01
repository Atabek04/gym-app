package com.epam.gym.util;

import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateEntityFields {
    public static void updateTraineeFields(Trainee existingTrainee, Trainee updatedTrainee) {
        updateUserFields(existingTrainee.getUser(), updatedTrainee.getUser());

        existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
        existingTrainee.setAddress(updatedTrainee.getAddress());
        existingTrainee.setTrainings(updatedTrainee.getTrainings());
    }

    public static void updateUserFields(User existingUser, User updatedUser) {
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setActive(updatedUser.isActive());
        existingUser.setRole(updatedUser.getRole());
    }

    public static void updateTrainerFields(Trainer existingTrainer, Trainer updatedTrainer) {
        updateUserFields(existingTrainer.getUser(), updatedTrainer.getUser());

        existingTrainer.setTrainingTypeId(updatedTrainer.getTrainingTypeId());
    }

    public static void updateTrainingFields(Training existingTraining, Training updatedTraining) {
        existingTraining.setTrainingName(updatedTraining.getTrainingName());
        existingTraining.setTrainingTypeId(updatedTraining.getTrainingTypeId());
        existingTraining.setTrainingDate(updatedTraining.getTrainingDate());
        existingTraining.setTrainingDuration(updatedTraining.getTrainingDuration());
        existingTraining.setTrainee(updatedTraining.getTrainee());
        existingTraining.setTrainer(updatedTraining.getTrainer());
    }
}
