package com.epam.gym.main.messaging;

import com.epam.gym.main.dto.AuthUserDTO;
import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final JmsTemplate jmsTemplate;

    public void sendToAuthService(AuthUserDTO message) {
        log.info("Sending message to auth-user-queue: {}", message);
        jmsTemplate.convertAndSend("auth-user-queue", message);
    }

    public void sendToTrainingReport(TrainerWorkloadRequest message) {
        log.info("Sending message to training-report-queue: {}", message);
        jmsTemplate.convertAndSend("training-report-queue", message);
    }
}