package com.epam.gym.trainingreport.service;

import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.dto.TrainerWorkloadResponse;
import com.epam.gym.trainingreport.exception.TrainerWorkloadNotFoundException;
import com.epam.gym.trainingreport.model.ActionType;
import com.epam.gym.trainingreport.model.TrainerWorkload;
import com.epam.gym.trainingreport.model.TrainingDuration;
import com.epam.gym.trainingreport.model.TrainingYear;
import com.epam.gym.trainingreport.repository.TrainerWorkloadRepository;
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

    public void createTrainerWorkload(TrainerWorkloadRequest request) {
        log.debug("Processing trainer workload with username: {}", request.getUsername());
        var workload = findOrCreateTrainerWorkload(request);

        var year = YearMonth.from(request.getTrainingDate()).getYear();
        var month = YearMonth.from(request.getTrainingDate()).getMonthValue();

        var trainingYear = findOrCreateTrainingYear(workload, year);
        updateTrainingDurations(trainingYear, month, request);
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
                    var newYear = TrainingYear.builder()
                            .year(year)
                            .trainingDurations(new ArrayList<>())
                            .build();
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
                        var newDuration = TrainingDuration.builder()
                                .month(month)
                                .totalDuration(request.getTrainingDuration())
                                .build();
                        trainingYear.getTrainingDurations().add(newDuration);
                    }
            );
        } else if (request.getActionType() == ActionType.DELETE) {
            existingDuration.ifPresent(trainingYear.getTrainingDurations()::remove);
        }
    }

    public TrainerWorkloadResponse getTrainerWorkload(String username) {
        log.debug("Getting trainer summary for user: {}", username);
        TrainerWorkload workload = repository.findByUsername(username)
                .orElseThrow(() -> new TrainerWorkloadNotFoundException("Trainer not found"));
        log.info("Trainer summary found for user");
        return TrainerWorkloadResponse.builder()
                .username(workload.getUsername())
                .yearlySummary(workload.getYearlySummary())
                .build();
    }
}
