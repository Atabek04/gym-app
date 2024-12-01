package com.epam.gym.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.epam.gym.util.Constants.FREE_DISK_SPACE;

@Component
public class DiskSpaceHealthIndicator implements HealthIndicator {

    private static final long THRESHOLD = 100L * 1024L * 1024L;  // 100 MB threshold

    @Override
    public Health health() {
        File disk = new File("/");
        long freeSpace = disk.getFreeSpace();

        if (freeSpace >= THRESHOLD) {
            return Health.up().withDetail(FREE_DISK_SPACE, freeSpace).build();
        } else {
            return Health.down().withDetail(FREE_DISK_SPACE, freeSpace).build();
        }
    }
}