package com.epam.gym.trainingreport;

import com.epam.gym.trainingreport.model.TrainerWorkload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // Ensure the correct profile is used for testing
class TrainerWorkloadDataInitializationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldInitializeTrainerWorkloadData() {
        // Query the database for trainer workloads
        List<TrainerWorkload> workloads = mongoTemplate.findAll(TrainerWorkload.class);

        // Assert that the data is initialized
        assertThat(workloads).isNotEmpty();
        assertThat(workloads.size()).isGreaterThanOrEqualTo(1);

        // Validate specific data (example)
        TrainerWorkload workload = workloads.get(0);
        assertThat(workload.getUsername()).isEqualTo("Peter.Parker");
        assertThat(workload.getFirstName()).isEqualTo("Peter");
        assertThat(workload.getLastName()).isEqualTo("Parker");
    }
}