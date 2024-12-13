package com.epam.gym.main.messaging;

import com.epam.gym.main.dto.AuthUserDTO;
import com.epam.gym.trainingreport.model.ActionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceNotifier {

    private final MessageProducer messageProducer;

    public void createUser(AuthUserDTO createAuthUserDTO) {
        messageProducer.sendToAuthService(createAuthUserDTO);
        log.info("Notified auth-service to create user: {}", createAuthUserDTO);
    }

    public void deleteUser(String username) {
        messageProducer.sendToAuthService(
                AuthUserDTO.builder()
                        .username(username)
                        .actionType(ActionType.DELETE)
                        .build()
        );
        log.info("Notified auth-service to delete user: {}", username);
    }
}