package com.epam.gym.trainingreport.messaging;

import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainingReportConsumer {

    private final TrainerWorkloadService service;

    @JmsListener(destination = "training-report-queue")
    public void processTrainingReportMessage(TrainerWorkloadRequest request) {
        log.info("Received message from training-report-queue: {}", request);
        service.processTraining(request);
    }
}
