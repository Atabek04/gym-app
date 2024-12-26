package com.epam.gym.main.service.impl;

import com.epam.gym.main.dto.AuthUserDTO;
import com.epam.gym.main.exception.ResourceNotFoundException;
import com.epam.gym.main.messaging.AuthServiceNotifier;
import com.epam.gym.main.model.User;
import com.epam.gym.main.model.UserRole;
import com.epam.gym.main.repository.UserRepository;
import com.epam.gym.main.service.UserService;
import com.epam.gym.trainingreport.model.ActionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.epam.gym.main.util.UserUtils.generateUsername;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final AuthServiceNotifier authServiceNotifier;

    @Override
    public Optional<User> create(User user, UserRole role, String plainPassword) {
        log.info("Creating new user with firstName {} and lastName {}", user.getFirstName(), user.getLastName());

        var username = generateUsername(
                user.getFirstName(),
                user.getLastName(),
                userRepo.findAllUsernames());
        user.setUsername(username);

        var authRequest = AuthUserDTO.builder()
                .username(username)
                .password(plainPassword)
                .role(role)
                .isActive(true)
                .actionType(ActionType.ADD)
                .build();

        authServiceNotifier.createUser(authRequest);

        var savedUser = userRepo.save(user);

        log.info("User {} with username: {} created successfully.", savedUser.getId(), savedUser.getUsername());
        return Optional.of(savedUser);
    }

    @Override
    public Optional<User> update(User user, Long userId) {
        var existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        log.info("Updating user with ID: {}", userId);

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());

        var updatedUser = userRepo.save(existingUser);
        return Optional.of(updatedUser);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user with ID: {}", id);

        userRepo.deleteById(id);

        log.info("User with ID: {} deleted successfully.", id);
    }
}
