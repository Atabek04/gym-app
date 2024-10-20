package com.epam.gym.controller;

import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserNewPasswordCredentials;
import com.epam.gym.exception.AuthenticationException;
import com.epam.gym.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate a user and return JWT and Refresh Token.")
    public Map<String, String> login(@Valid @RequestBody UserCredentials credentials) {
        return authService.login(credentials);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh Token", description = "Generate a new access token using a refresh token.")
    public Map<String, String> refreshToken(@ParameterObject @RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");
        return authService.refreshToken(refreshToken);
    }

    @PutMapping("/password")
    @Operation(summary = "Change Password", description = "Change the password of the authenticated user.")
    public void changePassword(@Valid @RequestBody UserNewPasswordCredentials credentials) {
        authService.changePassword(credentials);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Log out the authenticated user.")
    public String logout() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            authService.logout(username);
            return "User successfully logged out and refresh token deleted.";
        }
        throw new AuthenticationException("User is not authenticated.");
    }
}
