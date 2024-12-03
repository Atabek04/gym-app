package com.epam.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.epam.gym")
@EnableDiscoveryClient
@EnableFeignClients
public class GymApp {
    public static void main(String[] args) {
        SpringApplication.run(GymApp.class, args);
    }
}
