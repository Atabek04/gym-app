package com.epam.gym.main.mapper;

import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.TraineeUpdateRequest;
import com.epam.gym.main.dto.TrainerRequest;
import com.epam.gym.main.dto.TrainerUpdateRequest;
import com.epam.gym.main.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public static User toUser(TraineeRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }

    public static User toUser(TrainerRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }

    public static User toUser(TraineeUpdateRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }

    public static User toUser(TrainerUpdateRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }
}
