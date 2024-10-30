package com.epam.gym.controller.openapi;

import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserNewPasswordCredentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "Authentication")
public interface AuthApi {

    @Operation(summary = "Login", description = "Authenticate a user and return JWT and Refresh Token.")
    Map<String, String> login(@RequestBody UserCredentials credentials);

    @Operation(summary = "Refresh Token", description = "Generate a new access token using a refresh token.")
    Map<String, String> refreshToken(@ParameterObject @RequestBody Map<String, String> tokenRequest);

    @Operation(summary = "Change Password", description = "Change the password of the authenticated user.")
    void changePassword(@RequestBody UserNewPasswordCredentials credentials);

    @Operation(summary = "Logout", description = "Log out the authenticated user.")
    String logout(String username);
}