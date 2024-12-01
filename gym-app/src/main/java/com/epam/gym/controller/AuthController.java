package com.epam.gym.controller;

import com.epam.gym.controller.openapi.AuthApi;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserNewPasswordCredentials;
import com.epam.gym.exception.AuthenticationException;
import com.epam.gym.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserCredentials credentials) {
        return authService.login(credentials);
    }

    @Override
    @PostMapping("/refresh-token")
    public Map<String, String> refreshToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");
        return authService.refreshToken(refreshToken);
    }

    @Override
    @PutMapping("/password")
    public void changePassword(@RequestBody UserNewPasswordCredentials credentials) {
        authService.changePassword(credentials);
    }

    @Override
    @PostMapping("/logout")
    public String logout(@AuthenticationPrincipal String username) {
        if (username == null) {
            throw new AuthenticationException("User is not authenticated.");
        }
        authService.logout(username);
        return "User successfully logged out and refresh token deleted.";
    }
}
