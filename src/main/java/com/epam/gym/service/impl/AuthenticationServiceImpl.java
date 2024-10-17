package com.epam.gym.service.impl;

import com.epam.gym.exception.AuthenticationException;
import com.epam.gym.security.depricated.AuthenticatedUser;
import com.epam.gym.security.depricated.AuthenticationContext;
import com.epam.gym.service.AuthenticationService;
import com.epam.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;

    @Override
    public void authenticate(String authenticationHeader) {
        log.info("Authenticating request...");

        if (authenticationHeader != null && authenticationHeader.startsWith("Basic ")) {
            var base64Credentials = authenticationHeader.substring(6);
            var credentials = new String(Base64.getDecoder().decode(base64Credentials));
            var values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];

            var user = userService.findByUsernameAndPassword(username, password);
            if (user.isPresent()) {
                var loggedUser = user.get();
                var authenticatedUser = AuthenticatedUser.builder()
                        .id(loggedUser.getId())
                        .username(loggedUser.getUsername())
                        .role(loggedUser.getRole())
                        .build();
                AuthenticationContext.setAuthenticatedUser(authenticatedUser);
                log.info("User {} authenticated successfully.", username);
            } else {
                log.warn("Failed authentication attempt for user {}", username);
                throw new AuthenticationException("Invalid credentials. Wrong username or password");
            }
        } else {
            log.error("Missing or invalid Authorization header");
            throw new AuthenticationException("Missing or invalid Authorization header");
        }
    }
}

