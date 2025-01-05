package com.epam.gym.authservice.cucumber.stepdefinition;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class TestContext {

    private ResponseEntity<?> response;

}
