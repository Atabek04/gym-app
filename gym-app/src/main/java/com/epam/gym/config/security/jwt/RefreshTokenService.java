package com.epam.gym.config.security.jwt;

import com.epam.gym.exception.JWTException;
import com.epam.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    @Value("${security.jwt.refresh-token-expiration-ms}")
    private Long refreshTokenDurationMs;

    public String createRefreshToken(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        repository.save(refreshToken);

        return refreshToken.getToken();
    }

    public RefreshToken validateRefreshToken(String token) {
        var refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new JWTException("Refresh token doesn't exists in db"));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new JWTException("Your refresh token is expired");
        }
        return refreshToken;
    }

    @Transactional
    public void deleteByUser(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        repository.deleteByUser(user);
    }
}
