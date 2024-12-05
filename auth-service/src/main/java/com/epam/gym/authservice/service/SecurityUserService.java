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
        log.info("Creating SecurityUser with username: {}", authUserDTO.username());

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

        var savedUser = securityUserRepository.save(securityUser);

        log.info("SecurityUser with username: {} created successfully.", savedUser.getUsername());
        return savedUser;
    }

    public void deleteUserByUsername(String username) {
        log.info("Deleting SecurityUser with username: {}", username);

        var user = securityUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        securityUserRepository.delete(user);

        log.info("SecurityUser with username: {} deleted successfully.", username);
    }

    public boolean isUsernameTaken(String username) {
        log.info("Checking if username is taken: {}", username);
        return securityUserRepository.findByUsername(username).isPresent();
    }
}

