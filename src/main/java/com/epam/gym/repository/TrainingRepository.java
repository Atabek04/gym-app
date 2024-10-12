package com.epam.gym.repository;

import com.epam.gym.model.Training;
import com.epam.gym.model.TrainingTypeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    @Query("""
                SELECT t FROM Training t
                WHERE (:trainerId IS NULL OR t.trainer.id = :trainerId)
                AND (:traineeId IS NULL OR t.trainee.id = :traineeId)
                AND (:startDate IS NULL OR :endDate IS NULL OR t.trainingDate BETWEEN :startDate AND :endDate)
                AND (:trainingTypeId IS NULL OR t.trainingTypeId = :trainingTypeId)
            """)
    List<Training> findTrainingsByCriteria(Long trainerId,
                                           Long traineeId,
                                           LocalDate startDate,
                                           LocalDate endDate,
                                           Integer trainingTypeId,
                                           Sort sort);

    @Query("""
                SELECT t FROM Training t
                WHERE t.trainee.id = :traineeId
                AND (:periodFrom IS NULL OR t.trainingDate >= :periodFrom)
                AND (:periodTo IS NULL OR t.trainingDate <= :periodTo)
                AND (:trainerName IS NULL OR t.trainer.user.username = :trainerName)
                AND (:trainingTypeId IS NULL OR t.trainingTypeId = :trainingTypeId)
            """)
    List<Training> findTrainingsByFilters(Long traineeId,
                                          LocalDate periodFrom,
                                          LocalDate periodTo,
                                          String trainerName,
                                          Integer trainingTypeId);

    @Query("SELECT new TrainingTypeEntity(t.id, t.trainingTypeName) FROM TrainingTypeEntity t")
    List<TrainingTypeEntity> getAllTrainingTypes();

    @Query("FROM Training t WHERE t.trainee.user.username = :username")
    List<Training> findByTraineeUsername(String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM Training t WHERE t IN :trainings")
    void deleteAll(List<Training> trainings);
}
