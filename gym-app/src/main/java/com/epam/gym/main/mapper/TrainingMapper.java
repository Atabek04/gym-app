package com.epam.gym.main.mapper;

import com.epam.gym.main.dto.TrainingRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.model.Trainee;
import com.epam.gym.main.model.Trainer;
import com.epam.gym.main.model.Training;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrainingMapper {
    public static Training toTraining(TrainingRequest trainingRequest, Trainee trainee, Trainer trainer) {
        return Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(trainingRequest.trainingName())
                .trainingTypeId(trainer.getTrainingTypeId())
                .trainingDate(trainingRequest.trainingDate())
                .trainingDuration(trainingRequest.trainingDuration())
                .build();
    }

    public static TrainingResponse toTrainingResponse(Training training) {
        return TrainingResponse.builder()
                .id(training.getId())
                .traineeId(training.getTrainee().getId())
                .traineeFirstName(training.getTrainee().getUser().getFirstName())
                .traineeLastName(training.getTrainee().getUser().getLastName())
                .trainerId(training.getTrainer().getId())
                .trainerFirstName(training.getTrainer().getUser().getFirstName())
                .trainerLastName(training.getTrainer().getUser().getLastName())
                .trainingName(training.getTrainingName())
                .trainingType(training.getTrainingType())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .build();
    }
}
