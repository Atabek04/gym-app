package com.epam.gym.service;

import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserNewPasswordCredentials;
import com.epam.gym.exception.AuthenticationException;
import com.epam.gym.exception.ResourceNotFoundException;
import com.epam.gym.security.jwt.JwtUtil;
import com.epam.gym.security.jwt.RefreshTokenService;
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
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public Map<String, String> login(UserCredentials credentials) {
        var user = userService.findByUsername(credentials.username())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (!user.isAccountNonLocked() && user.getLockoutTime() != null) {
            // Check if 5 minutes have passed since lockout
            if (Duration.between(user.getLockoutTime(), LocalDateTime.now()).toMinutes() < 5) {
                throw new AuthenticationException("Account is locked. Please try again later.");
            } else {
                user.setAccountNonLocked(true);
                user.setFailedLoginAttempts(0);
                user.setLockoutTime(null);
                userService.create(user);
            }
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password())
            );

            user.setFailedLoginAttempts(0);
            user.setAccountNonLocked(true);
            userService.create(user);

            var userDetails = userDetailsService.loadUserByUsername(credentials.username());
            var jwt = jwtUtil.generateToken(userDetails, user.getRole());
            var refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("accessToken", jwt);
            responseBody.put("refreshToken", refreshToken);
            return responseBody;
        } catch (BadCredentialsException e) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);

            if (attempts >= 3) {
                user.setAccountNonLocked(false);
                user.setLockoutTime(LocalDateTime.now());
            }
            userService.create(user);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials. Attempts: " + attempts);
            return errorResponse;
        }
    }


    public Map<String, String> refreshToken(String refreshToken) {
        var token = refreshTokenService.validateRefreshToken(refreshToken);
        var userDetails = userDetailsService.loadUserByUsername(token.getUser().getUsername());
        var role = userService.findByUsername(token.getUser().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getRole();
        var newAccessToken = jwtUtil.generateToken(userDetails, role);
        return Map.of("accessToken", newAccessToken);
    }

    public void changePassword(UserNewPasswordCredentials credentials) {
        userService.validateAndChangePassword(credentials);
    }

    public void logout(String username) {
        refreshTokenService.deleteByUser(username);
    }
}
