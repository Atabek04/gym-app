package com.epam.gym.main.service.impl;

import com.epam.gym.main.dto.TrainingRequest;
import com.epam.gym.main.dto.TrainingTypeResponse;
import com.epam.gym.main.exception.ResourceNotFoundException;
import com.epam.gym.main.mapper.TrainingMapper;
import com.epam.gym.main.messaging.TrainingReportNotifier;
import com.epam.gym.main.model.Training;
import com.epam.gym.main.model.TrainingType;
import com.epam.gym.main.repository.TraineeRepository;
import com.epam.gym.main.repository.TrainerRepository;
import com.epam.gym.main.repository.TrainingRepository;
import com.epam.gym.main.service.TrainingService;
import com.epam.gym.main.util.UpdateEntityFields;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingReportNotifier trainingReportNotifier;

    @Override
    public void create(Training training) {
        log.info("Creating training for trainee: {} and trainer: {}", training.getTrainee().getId(), training.getTrainer().getId());
        trainingRepository.save(training);
        log.info("Training created successfully.");
    }

    @Override
    public void update(Training updatedTraining, Long id) {
        log.info("Updating training with ID: {}", id);

        Training existingTraining = trainingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training not found with ID: " + id));

        UpdateEntityFields.updateTrainingFields(existingTraining, updatedTraining);

        trainingRepository.save(existingTraining);
        log.info("Training with ID: {} updated successfully.", id);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting training with ID: {}", id);
        trainingRepository.deleteById(id);
        log.info("Training with ID: {} deleted successfully.", id);
    }

    @Override
    public void create(TrainingRequest request) {
        log.info("Creating training for request with trainer: {} and trainee: {}", request.trainerUsername(), request.traineeUsername());
        var trainer = trainerRepository.findByUserUsername(request.trainerUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        var trainee = traineeRepository.findByUserUsername(request.traineeUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        var training = trainingRepository.save(TrainingMapper.toTraining(request, trainee, trainer));
        log.info("Training created successfully for trainee: {} and trainer: {}", request.traineeUsername(), request.trainerUsername());

        trainingReportNotifier.addTrainerWorkload(training);
    }

    @Override
    public List<TrainingTypeResponse> getAllTrainingTypes() {
        log.info("Fetching all training types.");
        var trainings = trainingRepository.getAllTrainingTypes()
                .stream()
                .map(tr -> TrainingTypeResponse.builder()
                        .trainingTypeId(tr.getId().intValue())
                        .trainingType(TrainingType.valueOf(tr.getTrainingTypeName()))
                        .build()).toList();
        log.info("Successfully fetched all training types.");
        return trainings;
    }
}
