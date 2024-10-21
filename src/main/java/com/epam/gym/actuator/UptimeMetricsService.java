package com.epam.gym.actuator;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class UptimeMetricsService {
    private final long appStartTime;

    public UptimeMetricsService(MeterRegistry meterRegistry) {
        this.appStartTime = System.currentTimeMillis();
        Gauge.builder("app_uptime_seconds", this::getAppUptimeSeconds)
                .register(meterRegistry);
    }

    public double getAppUptimeSeconds() {
        return (System.currentTimeMillis() - appStartTime) / 1000.0;
    }
}
