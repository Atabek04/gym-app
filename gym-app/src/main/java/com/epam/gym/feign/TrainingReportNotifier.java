package com.epam.gym.feign;

import com.epam.gym.model.Training;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.model.ActionType;
import com.epam.gym.util.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainingReportNotifier {

    private final TrainingReportClient trainingReportClient;
    private final TrainingRepository trainingRepository;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    private final AtomicInteger attemptCounter = new AtomicInteger();

    public void removeTrainerWorkload(String username, UserType userType) {
        List<Training> relatedTrainings;
        if (userType == UserType.TRAINER) {
            relatedTrainings = trainingRepository.findTrainingsByTrainerUsername(username);
        } else {
            relatedTrainings = trainingRepository.findByTraineeUsername(username);
        }

        relatedTrainings.forEach(training -> {
            var request = TrainerWorkloadRequest.builder()
                    .username(training.getTrainer().getUser().getUsername())
                    .firstName(training.getTrainer().getUser().getFirstName())
                    .lastName(training.getTrainer().getUser().getLastName())
                    .isActive(false)
                    .trainingDate(training.getTrainingDate().toLocalDate())
                    .trainingDuration(training.getTrainingDuration().intValue())
                    .actionType(ActionType.DELETE)
                    .build();

            attemptCounter.incrementAndGet();

            var response = circuitBreakerFactory.create("handleTrainingDelete").run(
                    () -> {
                        log.info("Attempt #{}: Calling training-report for removal: {}", attemptCounter.get(), request);
                        trainingReportClient.handleTraining(request);
                        log.info("Attempt #{}: Successfully notified training-report while removing.", attemptCounter.get());
                        return "Success";
                    }, throwable -> {
                        log.error("Attempt #{}: Failed to notify training-report while removing for training: {}",
                                attemptCounter.get(), request, throwable);
                        return "Failure";
                    });

            log.info("Response from circuit breaker when removing workload: {}", response);
        });
    }

    public void addTrainerWorkload(Training training) {
        var request = TrainerWorkloadRequest.builder()
                .username(training.getTrainer().getUser().getUsername())
                .firstName(training.getTrainer().getUser().getFirstName())
                .lastName(training.getTrainer().getUser().getLastName())
                .isActive(true)
                .trainingDate(training.getTrainingDate().toLocalDate())
                .trainingDuration(training.getTrainingDuration().intValue())
                .actionType(ActionType.ADD)
                .build();

        attemptCounter.incrementAndGet();


        var response = circuitBreakerFactory.create("handleTrainingAdd").run(
                () -> {
                    log.info("Attempt #{}: Calling training-report for addition: {}", attemptCounter.get(), request);
                    trainingReportClient.handleTraining(request);
                    log.info("Attempt #{}: Successfully notified training-report while adding.", attemptCounter.get());
                    return "Success";
                }, throwable -> {
                    log.error("Attempt #{}: Failed to notify training-report while adding for training: {}",
                            attemptCounter.get(), request, throwable);
                    return "Failure";
                });

        log.info("Response from circuit breaker when adding workload: {}", response);
    }
}
