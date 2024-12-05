package com.epam.gym.apigateway.actuator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class R2dbcDatabaseHealthIndicator implements HealthIndicator {

    private final DatabaseClient databaseClient;

    @Override
    public Health health() {
        Map<String, Mono<Integer>> tableCounts = Map.of(
                "users_table", getTableRowCount("users"),
                "trainee_table", getTableRowCount("trainee"),
                "trainer_table", getTableRowCount("trainer"),
                "training_table", getTableRowCount("training"),
                "training_type_table", getTableRowCount("training_type")
        );

        Map<String, Object> details = new HashMap<>();
        for (Map.Entry<String, Mono<Integer>> entry : tableCounts.entrySet()) {
            entry.getValue()
                    .doOnError(e -> log.error("Error querying {}: {}", entry.getKey(), e.getMessage()))
                    .onErrorReturn(-1) // Return -1 if an error occurs
                    .subscribe(count -> details.put(entry.getKey(), count));
        }

        return Health.up().withDetails(details).build();
    }

    private Mono<Integer> getTableRowCount(String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        return databaseClient.sql(query)
                .map(row -> row.get(0, Integer.class)) // Map the first column
                .first(); // Retrieve the first result
    }
}
