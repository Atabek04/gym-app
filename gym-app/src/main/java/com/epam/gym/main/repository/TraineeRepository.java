package com.epam.gym.main.repository;

import com.epam.gym.main.model.Trainee;
import com.epam.gym.main.model.Trainer;
import com.epam.gym.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Optional<Trainee> findByUserUsername(String username);

    @Query("SELECT tr.trainer FROM Training tr WHERE tr.trainee.user.username = :username")
    List<Trainer> getAssignedTrainers(@Param("username") String username);

    @Query("""
        SELECT t FROM Trainer t WHERE t.id NOT IN (
            SELECT tr.trainer.id
            FROM Training tr
            WHERE tr.trainee.user.username = :username
        )
        """)
    List<Trainer> getNotAssignedTrainers(@Param("username") String username);

    @Transactional
    void deleteByUserUsername(String username);

    String user(User user);
}
