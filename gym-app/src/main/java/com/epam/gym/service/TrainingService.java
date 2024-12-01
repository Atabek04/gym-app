package com.epam.gym.service;

import com.epam.gym.dto.TrainingRequest;
import com.epam.gym.dto.TrainingTypeResponse;
import com.epam.gym.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    void create(Training training);

    void update(Training training, Long id);

    Optional<Training> findById(Long id);

    List<Training> findAll();


    List<Training> findTrainingsByCriteria(Long trainerId,
                                           Long traineeId,
                                           LocalDate startDate,
                                           LocalDate endDate,
                                           Integer typeId,
                                           String sortBy,
                                           boolean ascending);

    void delete(Long id);

    void create(TrainingRequest request);

    List<TrainingTypeResponse> getAllTrainingTypes();
}
