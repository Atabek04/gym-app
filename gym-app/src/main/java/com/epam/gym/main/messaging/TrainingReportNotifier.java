package com.epam.gym.main.messaging;

import com.epam.gym.main.model.Training;
import com.epam.gym.main.repository.TrainingRepository;
import com.epam.gym.main.util.UserType;
import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.model.ActionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrainingReportNotifier {

    private final MessageProducer messageProducer;
    private final TrainingRepository trainingRepository;

    public void removeTrainerWorkload(String username, UserType userType) {
        List<Training> relatedTrainings = userType == UserType.TRAINER
                ? trainingRepository.findTrainingsByTrainerUsername(username)
                : trainingRepository.findByTraineeUsername(username);

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
            messageProducer.sendToTrainingReport(request);
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
        messageProducer.sendToTrainingReport(request);
    }
}

