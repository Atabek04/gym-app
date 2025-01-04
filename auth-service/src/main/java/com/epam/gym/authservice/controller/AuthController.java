package com.epam.gym.authservice.controller;

import com.epam.gym.authservice.dto.AuthUserDTO;
import com.epam.gym.authservice.dto.UserCredentials;
import com.epam.gym.authservice.dto.UserNewPasswordCredentials;
import com.epam.gym.authservice.service.AuthService;
import com.epam.gym.authservice.service.SecurityUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController implements AuthApi {

    private final SecurityUserService securityUserService;
    private final AuthService authService;

    @PostMapping("/users")
    @ResponseStatus(CREATED)
    public void createUser(@Valid @RequestBody AuthUserDTO createAuthUserDTO) {
        log.info("Received request to create SecurityUser");
        securityUserService.createUser(createAuthUserDTO);
        log.info("Successfully created user");
    }

    @DeleteMapping("/users/{username}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUserByUsername(@NotBlank @PathVariable("username") String username) {
        log.info("Received request to delete SecurityUser");
        securityUserService.deleteUserByUsername(username);
        log.info("Successfully deleted user");
    }

    @GetMapping("/users/exists/{username}")
    public Boolean isUsernameTaken(@PathVariable("username") String username) {
        log.info("Received request to check if username is taken");
        return securityUserService.isUsernameTaken(username);
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody UserCredentials credentials) {
        log.info("Received request to login user with username: {}", credentials.username());
        return authService.login(credentials);
    }

    @PostMapping("/refresh-token")
    public Map<String, String> refreshToken(@NotBlank @RequestBody String refreshToken) {
        log.info("Received request to refresh token");
        return authService.refreshToken(refreshToken);
    }

    @PutMapping("/password")
    public void changePassword(@Valid @RequestBody UserNewPasswordCredentials credentials) {
        log.info("Received request to change password for user");
        authService.changePassword(credentials);
        log.info("Successfully changed password");
    }

    @PostMapping("/logout")
    public String logout(@AuthenticationPrincipal String username) {
        log.info("Received request to logout user");
        authService.logout(username);
        return "User successfully logged out and refresh token deleted.";
    }
}
