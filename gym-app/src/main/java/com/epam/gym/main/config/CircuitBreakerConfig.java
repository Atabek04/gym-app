package com.epam.gym.main.config;

import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class CircuitBreakerConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> circuitBreakerTrainingAdd() {
        return factory -> factory.addCircuitBreakerCustomizer(
                resilience4jCircuitBreaker -> resilience4jCircuitBreaker.getEventPublisher()
                        .onStateTransition(this::logStateTransition),
                "handleTrainingAdd"
        );
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> circuitBreakerTrainingRemove() {
        return factory -> factory.addCircuitBreakerCustomizer(
                resilience4jCircuitBreaker -> resilience4jCircuitBreaker.getEventPublisher()
                        .onStateTransition(this::logStateTransition),
                "handleTrainingDelete"
        );
    }

    private void logStateTransition(CircuitBreakerOnStateTransitionEvent event) {
        log.error("Circuit Breaker '{}' transitioned from {} to {}.",
                event.getCircuitBreakerName(),
                event.getStateTransition().getFromState(),
                event.getStateTransition().getToState());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
