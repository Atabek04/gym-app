package com.epam.gym.authservice.messaging;

import com.epam.gym.authservice.dto.ActionType;
import com.epam.gym.authservice.dto.AuthUserDTO;
import com.epam.gym.authservice.dto.AuthUserMessageDTO;
import com.epam.gym.authservice.service.SecurityUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthServiceConsumer {

    private final SecurityUserService securityUserService;

    @JmsListener(destination = "auth-user-queue")
    public void processAuthMessage(AuthUserMessageDTO message) {
        log.info("Received message from auth-user-queue: {}", message);
        if (message.getActionType() == ActionType.ADD) {
            var authUser = AuthUserDTO.builder()
                    .username(message.getUsername())
                    .password(message.getPassword())
                    .role(message.getRole())
                    .isActive(message.isActive())
                    .build();
            securityUserService.createUser(authUser);
        } else if (message.getActionType() == ActionType.DELETE) {
            securityUserService.deleteUserByUsername(message.getUsername());
        }
    }
}
