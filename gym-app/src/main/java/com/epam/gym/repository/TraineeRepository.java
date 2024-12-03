package com.epam.gym.repository;

import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByUserUsername(String username);

    @Query("SELECT tr.trainer FROM Training tr WHERE tr.trainee.user.username = :username")
    List<Trainer> getAssignedTrainers(String username);

    @Query("""
            SELECT t FROM Trainer t WHERE t.id NOT IN (
                SELECT tr.trainer.id
                FROM Training tr
                WHERE tr.trainee.user.username = :username
            )
            """)
    List<Trainer> getNotAssignedTrainers(String username);

    @Transactional
    void deleteByUserUsername(String username);
}
