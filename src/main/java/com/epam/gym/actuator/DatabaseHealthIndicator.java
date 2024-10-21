package com.epam.gym.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();

        details.put("user_table", getTableRowCount("user_table"));
        details.put("trainee_table", getTableRowCount("trainee"));
        details.put("trainer_table", getTableRowCount("trainer"));
        details.put("training_table", getTableRowCount("training"));
        details.put("training_type_table", getTableRowCount("training_type"));

        return Health.up().withDetails(details).build();
    }

    private Integer getTableRowCount(String tableName) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        } catch (Exception e) {
            return -1;
        }
    }
}