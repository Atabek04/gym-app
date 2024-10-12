package com.epam.gym.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import static com.epam.gym.util.Constants.AVAILABLE;
import static com.epam.gym.util.Constants.EXTERNAL_SERVICE;
import static com.epam.gym.util.Constants.NOT_AVAILABLE;

@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean isServiceUp = checkExternalServiceHealth();

        if (isServiceUp) {
            return Health.up().withDetail(EXTERNAL_SERVICE, AVAILABLE).build();
        } else {
            return Health.down().withDetail(EXTERNAL_SERVICE, NOT_AVAILABLE).build();
        }
    }

    private boolean checkExternalServiceHealth() {
        return true;
    }
}