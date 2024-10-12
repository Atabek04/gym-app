package com.epam.gym.repository;

import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByUserUsername(String username);

    @Query("SELECT t FROM Trainer t WHERE t.id NOT IN (SELECT tr.trainer.id FROM Training tr) " +
            "AND t.user.username <> :username")
    List<Trainer> findAllFreeTrainers(String username);

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

    @Query("""
                SELECT t FROM Training t
                JOIN t.trainer tr
                JOIN tr.user u
                LEFT JOIN t.trainee tn
                LEFT JOIN tn.user tu
                WHERE u.username = :username
                AND (:periodFrom IS NULL OR t.trainingDate >= :periodFrom)
                AND (:periodTo IS NULL OR t.trainingDate <= :periodTo)
                AND (:traineeName IS NULL OR LOWER(tu.username) LIKE LOWER(CONCAT('%', :traineeName, '%')))
            """)
    List<Training> findTrainerTrainingsByFilters(String username,
                                                 ZonedDateTime periodFrom,
                                                 ZonedDateTime periodTo,
                                                 String traineeName);
}
