package com.epam.gym.trainingreport.repository;

import com.epam.gym.trainingreport.model.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkload, Long> {
    Optional<TrainerWorkload> findByUsername(String username);
}
