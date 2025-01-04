package com.epam.gym.authservice.controller;

import com.epam.gym.authservice.dto.UserCredentials;
import com.epam.gym.authservice.dto.UserNewPasswordCredentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "Authentication")
@SuppressWarnings("unused")
public interface AuthApi {

    @Operation(summary = "Login", description = "Authenticate a user and return JWT and Refresh Token.")
    Map<String, String> login(@Valid @RequestBody UserCredentials credentials);

    @Operation(summary = "Refresh Token", description = "Generate a new access token using a refresh token.")
    Map<String, String> refreshToken(@Parameter(example = "4efea1be-4591-4ac2-89bd-73f200163f50") @NotBlank @RequestBody String refreshToken);

    @Operation(summary = "Change Password", description = "Change the password of the authenticated user.")
    void changePassword(@Valid @RequestBody UserNewPasswordCredentials credentials);

    @Operation(summary = "Logout", description = "Log out the authenticated user.")
    String logout(@Parameter(example = "Super.Man") String username);
}