package com.epam.gym.controller;

import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserNewPasswordCredentials;
import com.epam.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody UserCredentials credentials) {
        userService.findByUsernameAndPassword(credentials.username(), credentials.password());
    }

    @PutMapping("/password")
    public void changePassword(@Valid @RequestBody UserNewPasswordCredentials credentials) {
        userService.validateAndChangePassword(credentials);
    }
}
