package com.epam.gym.repository;

import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t WHERE t.id NOT IN (SELECT tr.trainer.id FROM Training tr) " +
            "AND t.user.username <> :username")
    List<Trainer> findAllFreeTrainers(String username);

    Optional<Trainer> findByUserUsername(String username);

    @Query("SELECT t.trainee FROM Training t WHERE t.trainer.user.username = :username")
    List<Trainee> getAssignedTrainees(String username);

}
