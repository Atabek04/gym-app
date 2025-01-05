package com.epam.gym.main;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
class DatabaseInitializationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testTraineeInitialization() {
        List<String> trainees = jdbcTemplate.query(
                "SELECT u.username FROM users_table u " +
                        "JOIN trainee t ON u.id = t.user_id",
                (rs, rowNum) -> rs.getString("username")
        );

        assertThat(trainees).containsExactlyInAnyOrder("Bruce.Wayne", "Clark.Kent", "Tony.Stark");
    }

    @Test
    void testTrainerInitialization() {
        List<String> trainers = jdbcTemplate.query(
                "SELECT u.username FROM users_table u " +
                        "JOIN trainer t ON u.id = t.user_id",
                (rs, rowNum) -> rs.getString("username")
        );

        assertThat(trainers).containsExactlyInAnyOrder("Diana.Prince", "Peter.Parker");
    }

    @Test
    void testTrainingTypesInitialization() {
        List<String> trainingTypes = jdbcTemplate.query(
                "SELECT training_type_name FROM training_type",
                (rs, rowNum) -> rs.getString("training_type_name")
        );

        assertThat(trainingTypes).containsExactlyInAnyOrder("Yoga", "Cardio", "Strength Training", "Pilates", "HIIT");
    }

    @Test
    void testTrainingSessionsInitialization() {
        List<String> trainingSessions = jdbcTemplate.query(
                "SELECT training_name FROM training",
                (rs, rowNum) -> rs.getString("training_name")
        );

        assertThat(trainingSessions).containsExactlyInAnyOrder("Morning Yoga", "Cardio Blast");
    }

    @Test
    void testTrainingAssociations() {
        List<String> results = jdbcTemplate.query(
                "SELECT u.username || ' trained by ' || t.username as session_info " +
                        "FROM training tr " +
                        "JOIN trainee ta ON tr.trainee_id = ta.id " +
                        "JOIN users_table u ON ta.user_id = u.id " +
                        "JOIN trainer trn ON tr.trainer_id = trn.id " +
                        "JOIN users_table t ON trn.user_id = t.id",
                (rs, rowNum) -> rs.getString("session_info")
        );

        assertThat(results).containsExactlyInAnyOrder(
                "Bruce.Wayne trained by Diana.Prince",
                "Clark.Kent trained by Peter.Parker"
        );
    }
}
