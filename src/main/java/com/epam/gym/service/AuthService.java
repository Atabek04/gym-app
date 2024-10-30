package com.epam.gym.service;

import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserNewPasswordCredentials;
import com.epam.gym.exception.AuthenticationException;
import com.epam.gym.exception.ResourceNotFoundException;
import com.epam.gym.config.security.jwt.JwtUtil;
import com.epam.gym.config.security.jwt.RefreshTokenService;
import com.epam.gym.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public Map<String, String> login(UserCredentials credentials) {
        var user = userService.findByUsername(credentials.username())
                .orElseThrow(() -> new AuthenticationException("User not found"));
        checkAccountLock(user);
        authenticateUser(credentials);
        return generateTokens(user);
    }

    private void checkAccountLock(User user) {
        if (!user.isAccountNonLocked() && user.getLockoutTime() != null) {
            if (Duration.between(user.getLockoutTime(), LocalDateTime.now()).toMinutes() < 5) {
                throw new AuthenticationException("Account is locked. Please try again later.");
            } else {
                user.setAccountNonLocked(true);
                user.setFailedLoginAttempts(0);
                user.setLockoutTime(null);
                userService.create(user);
            }
        }
    }

    private void authenticateUser(UserCredentials credentials) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password())
            );
        } catch (BadCredentialsException e) {
            handleFailedLoginAttempt(credentials.username());
        }
    }

    private void handleFailedLoginAttempt(String username) {
        var user = userService.findByUsername(username).orElseThrow();
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= 3) {
            user.setAccountNonLocked(false);
            user.setLockoutTime(LocalDateTime.now());
        }
        userService.create(user);

        throw new AuthenticationException("Invalid credentials. Attempts: " + attempts);
    }

    private Map<String, String> generateTokens(User user) {
        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        var jwt = JwtUtil.generateToken(userDetails, user.getRole());
        var refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", jwt);
        responseBody.put("refreshToken", refreshToken);
        return responseBody;
    }

    public Map<String, String> refreshToken(String refreshToken) {
        var token = refreshTokenService.validateRefreshToken(refreshToken);
        var userDetails = userDetailsService.loadUserByUsername(token.getUser().getUsername());
        var role = userService.findByUsername(token.getUser().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getRole();
        var newAccessToken = JwtUtil.generateToken(userDetails, role);
        return Map.of("accessToken", newAccessToken);
    }

    public void changePassword(UserNewPasswordCredentials credentials) {
        userService.validateAndChangePassword(credentials);
    }

    public void logout(String username) {
        refreshTokenService.deleteByUser(username);
    }
}
