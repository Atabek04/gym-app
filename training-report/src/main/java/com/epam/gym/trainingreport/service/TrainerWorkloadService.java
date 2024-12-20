package com.epam.gym.trainingreport.service;

import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.dto.TrainerWorkloadResponse;
import com.epam.gym.trainingreport.exception.TrainerWorkloadNotFoundException;
import com.epam.gym.trainingreport.model.ActionType;
import com.epam.gym.trainingreport.model.TrainerWorkload;
import com.epam.gym.trainingreport.model.TrainingDuration;
import com.epam.gym.trainingreport.model.TrainingYear;
import com.epam.gym.trainingreport.repository.TrainerWorkloadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;

    @Transactional
    public void processTraining(TrainerWorkloadRequest request) {
        log.info("Processing training for user: {}", request.getUsername());
        var workload = findOrCreateTrainerWorkload(request);
        var trainingYear = findOrCreateTrainingYear(workload, YearMonth.from(request.getTrainingDate()).getYear());
        updateTrainingDurations(trainingYear, YearMonth.from(request.getTrainingDate()).getMonthValue(), request);
        repository.save(workload);
        log.info("Training processed and saved for user: {}", request.getUsername());
    }

    private TrainerWorkload findOrCreateTrainerWorkload(TrainerWorkloadRequest request) {
        return repository.findByUsername(request.getUsername())
                .orElseGet(() -> {
                    log.debug("Creating new trainer workload for user: {}", request.getUsername());
                    return TrainerWorkload.builder()
                            .username(request.getUsername())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .isActive(request.getIsActive())
                            .yearlySummary(new ArrayList<>())
                            .build();
                });
    }

    private TrainingYear findOrCreateTrainingYear(TrainerWorkload workload, int year) {
        return workload.getYearlySummary().stream()
                .filter(y -> y.getYear() == year)
                .findFirst()
                .orElseGet(() -> {
                    TrainingYear newYear = new TrainingYear(null, year, workload, new ArrayList<>());
                    workload.getYearlySummary().add(newYear);
                    return newYear;
                });
    }

    private void updateTrainingDurations(TrainingYear trainingYear, int month, TrainerWorkloadRequest request) {
        Optional<TrainingDuration> existingDuration = trainingYear.getTrainingDurations().stream()
                .filter(t -> t.getMonth() == month)
                .findFirst();

        if (request.getActionType() == ActionType.ADD) {
            existingDuration.ifPresentOrElse(
                    duration -> duration.setTotalDuration(duration.getTotalDuration() + request.getTrainingDuration()),
                    () -> {
                        TrainingDuration newDuration = new TrainingDuration(month, request.getTrainingDuration(), trainingYear);
                        trainingYear.getTrainingDurations().add(newDuration);
                    }
            );
        } else if (request.getActionType() == ActionType.DELETE) {
            existingDuration.ifPresent(trainingYear.getTrainingDurations()::remove);
        }
    }

    public TrainerWorkloadResponse getTrainerSummary(String username) {
        TrainerWorkload workload = repository.findByUsername(username)
                .orElseThrow(() -> new TrainerWorkloadNotFoundException("Trainer not found"));

        return TrainerWorkloadResponse.builder()
                .username(workload.getUsername())
                .yearlySummary(workload.getYearlySummary())
                .build();
    }
}
