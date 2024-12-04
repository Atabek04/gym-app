package com.epam.gym.main.service;

import com.epam.gym.main.dto.TrainingRequest;
import com.epam.gym.main.dto.TrainingTypeResponse;
import com.epam.gym.main.model.Training;

import java.util.List;

public interface TrainingService {
    void create(Training training);

    void update(Training training, Long id);

    void delete(Long id);

    void create(TrainingRequest request);

    List<TrainingTypeResponse> getAllTrainingTypes();
}
