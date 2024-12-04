package com.epam.gym.authservice.controller;

import com.epam.gym.authservice.dto.AuthUserDTO;
import com.epam.gym.authservice.dto.UserCredentials;
import com.epam.gym.authservice.dto.UserNewPasswordCredentials;
import com.epam.gym.authservice.exception.AuthenticationException;
import com.epam.gym.authservice.service.AuthService;
import com.epam.gym.authservice.service.SecurityUserService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController implements AuthApi {

    private final SecurityUserService securityUserService;
    private final AuthService authService;

    @PostMapping("/users")
    @PermitAll
    public void createUser(@RequestBody AuthUserDTO createAuthUserDTO) {
        log.info("Received request to create SecurityUser with username: {}", createAuthUserDTO.username());
        securityUserService.createUser(createAuthUserDTO);
    }

    @DeleteMapping("/users/{username}")
    public void deleteUserByUsername(@PathVariable String username) {
        log.info("Received request to delete SecurityUser with username: {}", username);
        securityUserService.deleteUserByUsername(username);
    }

    @GetMapping("/users/exists/{username}")
    public Boolean isUsernameTaken(@PathVariable String username) {
        log.info("Received request to check if username is taken: {}", username);
        return securityUserService.isUsernameTaken(username);
    }

    @Override
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserCredentials credentials) {
        log.info("Received request to login user with username: {}", credentials.username());
        return authService.login(credentials);
    }

    @Override
    @PostMapping("/refresh-token")
    public Map<String, String> refreshToken(@RequestBody String refreshToken) {
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
