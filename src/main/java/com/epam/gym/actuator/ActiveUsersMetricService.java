package com.epam.gym.actuator;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActiveUsersMetricService {

    private final AtomicInteger activeUsers = new AtomicInteger(0);

    public ActiveUsersMetricService(MeterRegistry meterRegistry) {
        Gauge.builder("active_users", activeUsers, AtomicInteger::get)
                .description("Number of active users")
                .register(meterRegistry);
    }

    public void userLoggedIn() {
        activeUsers.incrementAndGet();
    }

    public void userLoggedOut() {
        activeUsers.decrementAndGet();
    }
}