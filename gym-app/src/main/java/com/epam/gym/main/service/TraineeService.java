package com.epam.gym.main.service;


import com.epam.gym.main.dto.BasicTrainerResponse;
import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.TraineeResponse;
import com.epam.gym.main.dto.TraineeTrainingFilterRequest;
import com.epam.gym.main.dto.TraineeUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.model.Trainee;
import com.epam.gym.main.model.Trainer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    @Transactional
    UserCredentials create(TraineeRequest request);

    void update(Trainee trainee, Long id);

    TraineeResponse updateTraineeAndUser(TraineeUpdateRequest request, String username);

    Optional<Trainee> findByUsername(String username);

    @Transactional
    TraineeResponse getTraineeWithTrainers(String username);

    List<Trainer> getAssignedTrainers(String username);

    void delete(String username);

    List<BasicTrainerResponse> getNotAssignedTrainers(String username);

    void updateTrainers(String username, List<String> trainerUsernames);

    List<TrainingResponse> getTraineeTrainings(String username, TraineeTrainingFilterRequest filterRequest);
}
