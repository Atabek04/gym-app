package com.epam.gym.main.util;

import com.epam.gym.main.model.Trainer;
import com.epam.gym.main.model.Training;
import com.epam.gym.main.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateEntityFields {

    public static void updateUserFields(User existingUser, User updatedUser) {
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
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
