package com.epam.gym.authservice.service;

import com.epam.gym.authservice.dto.UserCredentials;
import com.epam.gym.authservice.dto.UserNewPasswordCredentials;
import com.epam.gym.authservice.exception.AuthenticationException;
import com.epam.gym.authservice.exception.ResourceNotFoundException;
import com.epam.gym.authservice.model.SecurityUser;
import com.epam.gym.authservice.repository.SecurityUserRepository;
import com.epam.gym.authservice.security.CustomUserDetailsService;
import com.epam.gym.authservice.security.jwt.JwtUtil;
import com.epam.gym.authservice.security.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final SecurityUserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public Map<String, String> login(UserCredentials credentials) {
        var user = userRepository.findByUsername(credentials.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        checkAccountLock(user);
        authenticateUser(credentials);
        log.info("Generating tokens for user: {}", credentials.username());
        return generateTokens(user);
    }

    private void checkAccountLock(SecurityUser user) {
        if (!user.isAccountNonLocked() && user.getLockoutTime() != null) {
            if (Duration.between(user.getLockoutTime(), LocalDateTime.now()).toMinutes() < 5) {
                log.error("Account is locked for user: {}", user.getUsername());
                throw new AuthenticationException("Account is locked. Please try again later.");
            } else {
                user.setAccountNonLocked(true);
                user.setFailedLoginAttempts(0);
                user.setLockoutTime(null);
                userRepository.save(user);
            }
        }
    }

    private void authenticateUser(UserCredentials credentials) {
        try {
            log.info("Authenticating user: {} {}", credentials.username(), credentials.password());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password())
            );
            log.info("User authenticated: {}", credentials.username());
        } catch (BadCredentialsException e) {
            log.error("Failed login attempt for user: {}", credentials.username());
            handleFailedLoginAttempt(credentials.username());
        }
    }

    private void handleFailedLoginAttempt(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= 3) {
            user.setAccountNonLocked(false);
            user.setLockoutTime(LocalDateTime.now());
        }
        userRepository.save(user);

        log.error("Invalid credentials for user: {}. Attempts: {}", username, attempts);
        throw new AuthenticationException("Invalid credentials. Attempts: " + attempts);
    }

    private Map<String, String> generateTokens(SecurityUser user) {
        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        log.info("User details loaded for user: {}", userDetails.getUsername());

        var jwt = jwtUtil.generateToken(userDetails, user.getRole());
        var refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", jwt);
        responseBody.put("refreshToken", refreshToken);
        log.info("Tokens generated for user: {}", userDetails.getUsername());
        return responseBody;
    }

    public Map<String, String> refreshToken(String refreshToken) {
        var token = refreshTokenService.validateRefreshToken(refreshToken);
        var userDetails = userDetailsService.loadUserByUsername(token.getUser().getUsername());
        var role = userRepository.findByUsername(token.getUser().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getRole();
        var newAccessToken = jwtUtil.generateToken(userDetails, role);
        return Map.of("accessToken", newAccessToken);
    }

    public void changePassword(UserNewPasswordCredentials credentials) {
        log.info("Password change requested for user: {}", credentials.username());
        var user = userRepository.findByUsername(credentials.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + credentials.username()));

        if (passwordEncoder.matches(credentials.oldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(credentials.newPassword()));
            userRepository.save(user);
        } else {
            log.error("Old password mismatch for user: {}", credentials.username());
            throw new AuthenticationException("Authentication failed. Incorrect old password.");
        }
    }

    public void logout(String username) {
        refreshTokenService.deleteByUser(username);
    }
}
