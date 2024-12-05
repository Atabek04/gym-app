package com.epam.gym.main.service;

import com.epam.gym.main.dto.TrainerRequest;
import com.epam.gym.main.dto.TrainerResponse;
import com.epam.gym.main.dto.TrainerTrainingFilterRequest;
import com.epam.gym.main.dto.TrainerUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer create(Trainer trainer);

    UserCredentials create(TrainerRequest request);

    TrainerResponse getTrainerAndTrainees(String username);

    List<TrainingResponse> findTrainerTrainings(String username, TrainerTrainingFilterRequest filterRequest);

    void update(Trainer trainer, Long id);

    TrainerResponse updateTrainerAndUser(TrainerUpdateRequest request, String username);

    void delete(Long id);

    void delete(String username);
}
