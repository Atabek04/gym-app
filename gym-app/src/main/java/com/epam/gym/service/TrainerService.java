package com.epam.gym.service;

import com.epam.gym.dto.TrainerRequest;
import com.epam.gym.dto.TrainerResponse;
import com.epam.gym.dto.TrainerTrainingFilterRequest;
import com.epam.gym.dto.TrainerUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer create(Trainer trainer);

    UserCredentials create(TrainerRequest request);


    Optional<Trainer> findById(Long id);

    Optional<Trainer> findByUsername(String username);

    List<Trainer> findAll();

    List<Trainer> findAllFreeTrainers(String username);

    TrainerResponse getTrainerAndTrainees(String username);


    List<TrainingResponse> findTrainerTrainings(String username, TrainerTrainingFilterRequest filterRequest);

    void update(Trainer trainer, Long id);

    void updateTrainerStatus(String username, Boolean active);

    TrainerResponse updateTrainerAndUser(TrainerUpdateRequest request, String username);

    void delete(Long id);

    void delete(String username);
}
