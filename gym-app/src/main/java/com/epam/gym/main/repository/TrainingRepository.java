package com.epam.gym.main.repository;

import com.epam.gym.main.model.Training;
import com.epam.gym.main.model.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT new TrainingTypeEntity(t.id, t.trainingTypeName) FROM TrainingTypeEntity t")
    List<TrainingTypeEntity> getAllTrainingTypes();

    @Query("FROM Training t WHERE t.trainee.user.username = :username")
    List<Training> findByTraineeUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM Training t WHERE t IN :trainings")
    void deleteAll(List<Training> trainings);

    @Query("SELECT t FROM Training t WHERE t.trainee.id = :traineeId")
    List<Training> findByTraineeId(@Param("traineeId") Long traineeId);

    @Query("""
                SELECT t FROM Training t
                WHERE t.trainee.id = :traineeId
                AND t.trainingDate BETWEEN :periodFrom AND :periodTo
            """)
    List<Training> findByTraineeIdAndTrainingDateBetween(
            @Param("traineeId") Long traineeId,
            @Param("periodFrom") LocalDateTime periodFrom,
            @Param("periodTo") LocalDateTime periodTo);

    @Query("""
                SELECT t FROM Training t
                WHERE t.trainee.id = :traineeId
                AND t.trainingDate BETWEEN :periodFrom AND :periodTo
                AND t.trainer.user.username = :trainerName
                AND t.trainingTypeId = :trainingTypeId
            """)
    List<Training> findByAllFilters(
            @Param("traineeId") Long traineeId,
            @Param("periodFrom") LocalDateTime periodFrom,
            @Param("periodTo") LocalDateTime periodTo,
            @Param("trainerName") String trainerName,
            @Param("trainingTypeId") Integer trainingTypeId);

    @Query("""
                SELECT t FROM Training t
                WHERE t.trainer.user.username = :trainerUsername
            """)
    List<Training> findTrainingsByTrainerUsername(@Param("trainerUsername") String trainerUsername);

    @Query("""
                SELECT t FROM Training t
                WHERE t.trainer.user.username = :trainerUsername
                AND t.trainingDate BETWEEN :startDate AND :endDate
            """)
    List<Training> findTrainingsByTrainerUsernameAndPeriod(
            @Param("trainerUsername") String trainerUsername,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
