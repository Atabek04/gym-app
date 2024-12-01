package com.epam.gym.actuator;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class UptimeMetricsService {

    private final Instant appStartTime;

    public UptimeMetricsService(MeterRegistry meterRegistry) {
        this.appStartTime = Instant.now();
        Gauge.builder("app_uptime_seconds", this::getAppUptimeSeconds)
                .register(meterRegistry);
    }

    public double getAppUptimeSeconds() {
        return Duration.between(appStartTime, Instant.now()).getSeconds();
    }
}