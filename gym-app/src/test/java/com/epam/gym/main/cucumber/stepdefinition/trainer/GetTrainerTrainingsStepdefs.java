package com.epam.gym.main.cucumber.stepdefinition.trainer;

import com.epam.gym.main.cucumber.stepdefinition.TestContext;
import com.epam.gym.main.dto.TrainingResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class GetTrainerTrainingsStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;

    @When("the client sends a GET request to get trainer trainings {string}")
    public void theClientSendsAGETRequestToGetTrainerTrainings(String endpoint) {
        String baseUrl = "http://localhost:9876/api/v1";
        try {
            var url = baseUrl + endpoint;
            var response = restTemplate.getForEntity(url, TrainingResponse[].class);
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("the response contains all trainings for the trainer")
    public void theResponseContainsAllTrainingsForTheTrainer() {
        @SuppressWarnings("unchecked")
        var response = (ResponseEntity<TrainingResponse[]>) testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var trainings = response.getBody();
        assert trainings != null;
        assertThat(trainings.length).isGreaterThan(0);
    }
}
