package com.epam.gym.main.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@TestPropertySource(properties = "spring.profiles.active=test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@SuppressWarnings("unused")
public class CucumberSpringConfiguration {
}