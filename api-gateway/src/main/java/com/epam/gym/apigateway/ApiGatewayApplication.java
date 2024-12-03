package com.epam.gym.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(scanBasePackages = "com.epam.gym.apigateway")
@ComponentScan(
        basePackages = "com.epam.gym.apigateway",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.epam.gym.*")
)
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
