package com.epam.gym.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Value("${cors.allowed.methods}")
    private String[] allowedMethods;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/h2-console/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowCredentials(true);

        registry.addMapping("/v1/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowCredentials(true);

        registry.addMapping("/swagger-ui/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowCredentials(true);

        registry.addMapping("/v3/api-docs/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowCredentials(true);
    }
}
