package com.epam.gym.service.impl;

import com.epam.gym.dto.UserNewPasswordCredentials;
import com.epam.gym.exception.AuthenticationException;
import com.epam.gym.exception.ResourceNotFoundException;
import com.epam.gym.model.User;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.gym.util.UserUtils.generateRandomPassword;
import static com.epam.gym.util.UserUtils.generateUsername;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public User saveUser(User user) {
        if (user.getId() == null) {
            log.info("Creating new user with firstName: {} and lastName: {}", user.getFirstName(), user.getLastName());
            var username = generateUsername(
                    user.getFirstName(),
                    user.getLastName(),
                    userRepo.findAllUsernames());
            user.setUsername(username);
            user.setPassword(generateRandomPassword());
        } else {
            log.info("Updating existing user with ID: {}", user.getId());
        }

        var savedUser = userRepo.save(user);

        log.info("User {} with username: {} created/updated successfully.", savedUser.getId(), savedUser.getUsername());
        return savedUser;
    }

    @Override
    public Optional<User> create(User user) {
        return Optional.ofNullable(saveUser(user));
    }

    @Override
    public Optional<User> update(User user, Long id) {
        var oldUser = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setUsername(oldUser.getUsername());
        user.setPassword(oldUser.getPassword());
        user.setId(id);

        return Optional.ofNullable(saveUser(user));
    }

    @Override
    public Optional<User> findById(Long id) {
        log.info("Fetching user by ID: {}", id);
        return userRepo.findById(id);
    }

    @Override
    public List<User> findAll() {
        log.info("Fetching all users.");
        return userRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user with ID: {}", id);
        userRepo.deleteById(id);
        log.info("User with ID: {} deleted successfully.", id);
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        log.info("Authenticating user with username: {}", username);
        var user = Optional.ofNullable(userRepo.findByUsernameAndPassword(username, password));
        if (user.isEmpty()) {
            log.warn("Failed login attempt for user: {}", username);
            throw new AuthenticationException("Failed to login with username: " + username);
        }
        log.info("Successful login for user: {}", username);
        return user;
    }

    @Override
    public void changePassword(String username, String newPassword) {
        log.info("Changing password for user with username: {}", username);
        userRepo.changePassword(username, newPassword);
        log.info("Password changed successfully for username: {}", username);
    }

    @Override
    public void activateUser(String username) {
        log.info("Activating user with username: {}", username);
        userRepo.activateUser(username);
        log.info("User with username: {} activated.", username);
    }

    @Override
    public void deactivateUser(String username) {
        log.info("Deactivating user with username: {}", username);
        userRepo.deactivateUser(username);
        log.info("User with username: {} deactivated.", username);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return Optional.ofNullable(userRepo.findByUsername(username));
    }

    @Override
    public void validateAndChangePassword(UserNewPasswordCredentials credentials) {
        log.info("Password change requested for user: {}", credentials.username());
        log.info("Validating old password and changing password for user with username: {}", credentials.username());
        var user = findUserByUsername(credentials.username())
                .orElseThrow(() -> new ResourceNotFoundException("User with this username not found"));
        if (user.getPassword().equals(credentials.oldPassword())) {
            changePassword(credentials.username(), credentials.newPassword());
        } else {
            log.error("Old password mismatch for user: {}", credentials.username());
            throw new AuthenticationException("Failed to auth. Wrong old password");
        }
        log.info("Password changed successfully for user: {}", credentials.username());
    }
}
