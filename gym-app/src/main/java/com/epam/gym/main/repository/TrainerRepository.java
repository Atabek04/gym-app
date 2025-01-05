package com.epam.gym.main.repository;

import com.epam.gym.main.model.Trainee;
import com.epam.gym.main.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByUserUsername(String username);

    @Query("SELECT t.trainee FROM Training t WHERE t.trainer.user.username = :username")
    List<Trainee> getAssignedTrainees(@Param("username") String username);

}
