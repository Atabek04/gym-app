package com.epam.gym.main.repository;

import com.epam.gym.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();
}
