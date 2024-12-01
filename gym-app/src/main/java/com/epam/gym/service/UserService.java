package com.epam.gym.service;

import com.epam.gym.dto.UserNewPasswordCredentials;
import com.epam.gym.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> create(User user);

    Optional<User> update(User user, Long id);

    Optional<User> findById(Long id);

    List<User> findAll();

    void delete(Long id);

    Optional<User> findByUsernameAndPassword(String username, String password);

    void changePassword(String username, String newPassword);

    void activateUser(String username);

    void deactivateUser(String username);

    Optional<User> findByUsername(String username);

    void validateAndChangePassword(UserNewPasswordCredentials credentials);
}
