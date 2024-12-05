package com.epam.gym.main.mapper;

import com.epam.gym.main.dto.BasicTraineeResponse;
import com.epam.gym.main.dto.BasicTrainerResponse;
import com.epam.gym.main.dto.TrainerRequest;
import com.epam.gym.main.dto.TrainerResponse;
import com.epam.gym.main.dto.TrainerUpdateRequest;
import com.epam.gym.main.model.Trainee;
import com.epam.gym.main.model.Trainer;
import com.epam.gym.main.model.TrainingType;
import com.epam.gym.main.model.User;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TrainerMapper {
    public static Trainer toTrainer(TrainerRequest trainerRequest, User user) {
        return Trainer.builder()
                .user(user)
                .trainingTypeId(TrainingType.valueOf(trainerRequest.specialization()).getId())
                .build();
    }

    public static Trainer toTrainer(TrainerUpdateRequest trainerRequest, User user) {
        return Trainer.builder()
                .user(user)
                .trainingTypeId(TrainingType.valueOf(trainerRequest.specialization().toUpperCase()).getId())
                .build();
    }

    public static TrainerResponse toTrainerResponse(Trainer trainer, List<Trainee> trainees) {
        var traineeList = trainees.stream()
                .map(trainee -> BasicTraineeResponse.builder()
                        .username(trainee.getUser().getUsername())
                        .firstName(trainee.getUser().getFirstName())
                        .lastName(trainee.getUser().getLastName())
                        .dateOfBirth(trainee.getDateOfBirth().toString())
                        .address(trainee.getAddress())
                        .build())
                .toList();

        return TrainerResponse.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .username(trainer.getUser().getUsername())
                .specialization(trainer.getTrainingType().toString())
                .trainees(traineeList)
                .build();
    }

    public static BasicTrainerResponse toBasicTrainerResponse(Trainer trainer) {
        return BasicTrainerResponse.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .username(trainer.getUser().getUsername())
                .specialization(trainer.getTrainingType().toString())
                .build();
    }
}
