package com.epam.gym.main.cucumber.stepdefinition.trainer;

import com.epam.gym.main.cucumber.stepdefinition.trainee.TestContext;
import com.epam.gym.main.dto.TrainerRequest;
import com.epam.gym.main.dto.UserCredentials;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class CreateTrainerStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private TrainerRequest trainerRequest;

    @Given("a valid trainer request payload")
    public void aValidTrainerRequestPayload() {
        trainerRequest = TrainerRequest.builder()
                .firstName("Super")
                .lastName("Trainer")
                .specialization("CARDIO")
                .build();
    }

    @When("the client sends a POST request to create trainer at {string}")
    public void theClientSendsAPOSTRequestToCreateTrainerAt(String url) {
        String baseUrl = "http://localhost:9876/api/v1";
        var response = restTemplate.postForEntity(baseUrl + url, trainerRequest, UserCredentials.class);
        testContext.setResponse(response);
    }

    @And("the trainer is created in the system")
    public void theTrainerIsCreatedInTheSystem() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(201);

        var credentials = (UserCredentials) response.getBody();
        assert credentials != null;
        assertThat(credentials.username()).isEqualTo("Super.Trainer");
    }

    @Given("an invalid trainer request payload")
    public void anInvalidTrainerRequestPayload() {
        trainerRequest = TrainerRequest.builder()
                .firstName("")
                .lastName("")
                .specialization("")
                .build();
    }
}