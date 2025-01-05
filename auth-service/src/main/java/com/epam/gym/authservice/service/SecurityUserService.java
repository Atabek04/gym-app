package com.epam.gym.authservice.service;

import com.epam.gym.authservice.dto.AuthUserDTO;
import com.epam.gym.authservice.exception.ResourceNotFoundException;
import com.epam.gym.authservice.model.SecurityUser;
import com.epam.gym.authservice.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityUserService {

    private final SecurityUserRepository securityUserRepository;
    private final PasswordEncoder passwordEncoder;

    @SuppressWarnings("UnusedReturnValue")
    public SecurityUser createUser(AuthUserDTO authUserDTO) {
        log.debug("Creating SecurityUser with username: {}", authUserDTO.username());

        if (securityUserRepository.findByUsername(authUserDTO.username()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken: " + authUserDTO.username());
        }

        var securityUser = SecurityUser.builder()
                .username(authUserDTO.username())
                .password(passwordEncoder.encode(authUserDTO.password()))
                .role(authUserDTO.role())
                .isActive(authUserDTO.isActive())
                .failedLoginAttempts(0)
                .isAccountNonLocked(true)
                .build();

        return securityUserRepository.save(securityUser);
    }

    public void deleteUserByUsername(String username) {
        log.debug("Deleting SecurityUser with username: {}", username);

        var user = securityUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        securityUserRepository.delete(user);
    }

    public boolean isUsernameTaken(String username) {
        log.debug("Checking if username is taken: {}", username);
        var isTaken = securityUserRepository.findByUsername(username).isPresent();
        log.debug("Successfully checked. Username {} isTaken status: {}", username, isTaken);
        return isTaken;
    }
}

