package com.epam.gym.actuator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Health health() {
        Map<String, Object> details = Map.of(
                "user_table", getTableRowCount("user_table"),
                "trainee_table", getTableRowCount("trainee"),
                "trainer_table", getTableRowCount("trainer"),
                "training_table", getTableRowCount("training"),
                "training_type_table", getTableRowCount("training_type")
        );

        return Health.up().withDetails(details).build();
    }

    private Integer getTableRowCount(String tableName) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        } catch (Exception e) {
            log.error("Failed to retrieve row count for table: {}. Error: {}", tableName, e.getMessage(), e);
            return -1;
        }
    }
}