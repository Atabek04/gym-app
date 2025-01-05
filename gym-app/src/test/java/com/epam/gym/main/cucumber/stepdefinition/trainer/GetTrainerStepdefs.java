package com.epam.gym.main.cucumber.stepdefinition.trainer;

import com.epam.gym.main.cucumber.stepdefinition.trainee.TestContext;
import com.epam.gym.main.dto.TrainerResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class GetTrainerStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;

    @Given("an existing trainer with username {string}")
    public void anExistingTrainerWithUsername(String username) {
        testContext.setUsername(username);
    }

    @Given("a non-existing trainer with username {string}")
    public void aNonExistingTrainerWithUsername(String username) {
        testContext.setUsername(username);
    }

    @When("the client sends a GET request to get trainer at {string}")
    public void theClientSendsAGETRequestToGetTrainerAt(String url) {
        var username = testContext.getUsername();
        var baseUrl = "http://localhost:9876/api/v1";
        var response = restTemplate.getForEntity(baseUrl + url + username, TrainerResponse.class);
        testContext.setResponse(response);
    }

    @And("the response contains the trainer's details and trainees")
    public void theResponseContainsTheTrainerSDetailsAndTrainees() {
        var username = testContext.getUsername();
        @SuppressWarnings("unchecked")
        var response = (ResponseEntity<TrainerResponse>) testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var trainerResponse = response.getBody();
        assert trainerResponse != null;
        assertThat(trainerResponse.firstName()).isNotEmpty();
        assertThat(trainerResponse.lastName()).isNotEmpty();
        assertThat(trainerResponse.username()).isEqualTo(username);
    }
}