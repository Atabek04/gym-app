package com.epam.gym.main.mapper;

import com.epam.gym.main.dto.BasicTrainerResponse;
import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.TraineeResponse;
import com.epam.gym.main.dto.TraineeUpdateRequest;
import com.epam.gym.main.model.Trainee;
import com.epam.gym.main.model.Trainer;
import com.epam.gym.main.model.User;
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
                        .specialization(trainer.getTrainingType().toString())
                        .build())
                .toList();

        return TraineeResponse.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUsername())
                .dateOfBirth(trainee.getDateOfBirth().toString())
                .address(trainee.getAddress())
                .trainers(trainerList)
                .build();
    }
}
