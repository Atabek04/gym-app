package com.epam.gym.main.service;

import com.epam.gym.main.model.User;
import com.epam.gym.main.model.UserRole;

import java.util.Optional;

public interface UserService {

    Optional<User> create(User user, UserRole userRole, String plainPassword);

    Optional<User> update(User user, Long id);

    void delete(Long id);

}
