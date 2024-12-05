package com.epam.gym.authservice.security.jwt;

import com.epam.gym.authservice.model.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(SecurityUser user);
}
