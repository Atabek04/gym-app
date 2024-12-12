package com.epam.gym.trainingreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TrainingReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingReportApplication.class, args);
    }

}
