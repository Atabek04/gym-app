package com.epam.gym.mapper;

import com.epam.gym.dto.TraineeRequest;
import com.epam.gym.dto.TraineeUpdateRequest;
import com.epam.gym.dto.TrainerRequest;
import com.epam.gym.dto.TrainerUpdateRequest;
import com.epam.gym.model.User;
import com.epam.gym.model.UserRole;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public static User toUser(TraineeRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .isActive(true)
                .role(UserRole.ROLE_TRAINEE)
                .build();
    }

    public static User toUser(TrainerRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .isActive(true)
                .role(UserRole.ROLE_TRAINER)
                .build();
    }

    public static User toUser(TraineeUpdateRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .isActive(request.isActive())
                .role(UserRole.ROLE_TRAINER)
                .build();
    }

    public static User toUser(TrainerUpdateRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .isActive(request.isActive())
                .role(UserRole.ROLE_TRAINER)
                .build();
    }
}
