package com.epam.gym.mapper;

import com.epam.gym.dto.BasicTrainerResponse;
import com.epam.gym.dto.TraineeRequest;
import com.epam.gym.dto.TraineeResponse;
import com.epam.gym.dto.TraineeUpdateRequest;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class TraineeMapper {
    public static Trainee toTrainee(TraineeRequest request, User user) {
        var traineeBuilder = Trainee.builder()
                .user(user)
                .dateOfBirth(request.dateOfBirth());

        if (request.address() == null) {
            traineeBuilder.address(" ");
        } else {
            traineeBuilder.address(request.address());
        }
        if (request.dateOfBirth() == null) {
            traineeBuilder.dateOfBirth(LocalDate.now());
        } else {
            traineeBuilder.dateOfBirth(request.dateOfBirth());
        }

        return traineeBuilder.build();
    }

    public static Trainee toTrainee(TraineeUpdateRequest request, User user) {
        return Trainee.builder()
                .user(user)
                .dateOfBirth(request.dateOfBirth())
                .address(request.address())
                .build();
    }

    public static TraineeResponse toTraineeResponse(Trainee trainee, List<Trainer> trainers) {
        var trainerList = trainers.stream()
                .map(trainer -> BasicTrainerResponse.builder()
                        .firstName(trainer.getUser().getFirstName())
                        .lastName(trainer.getUser().getLastName())
                        .username(trainer.getUser().getUsername())
                        .isActive(trainer.getUser().isActive())
                        .specialization(trainer.getTrainingType().toString())
                        .build())
                .toList();

        return TraineeResponse.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUsername())
                .isActive(trainee.getUser().isActive())
                .dateOfBirth(trainee.getDateOfBirth().toString())
                .address(trainee.getAddress())
                .trainers(trainerList)
                .build();
    }
}
