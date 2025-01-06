package com.epam.gym.main.cucumber.stepdefinition.training;

import com.epam.gym.main.cucumber.stepdefinition.TestContext;
import com.epam.gym.main.dto.TrainingRequest;
import com.epam.gym.main.dto.TrainingTypeResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@RequiredArgsConstructor
public class TrainingStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private TrainingRequest trainingRequest;

    private final String baseUrl = "http://localhost:9876";

    @Given("a valid training request")
    public void aValidTrainingRequest() {
        trainingRequest = TrainingRequest.builder()
                .traineeUsername("Bruce.Wayne")
                .trainerUsername("Diana.Prince")
                .trainingName("Morning Yoga")
                .trainingDate(LocalDateTime.parse("2025-01-15T10:00:00"))
                .trainingDuration(90L)
                .build();
    }

    @When("the client sends a POST request to create training at {string}")
    public void theClientSendsAPOSTRequestToCreateTrainingAt(String url) {
        try {
            var response = restTemplate.postForEntity(baseUrl + url, trainingRequest, Void.class);
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("the training is successfully created")
    public void theTrainingIsSuccessfullyCreated() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Given("an invalid training request")
    public void anInvalidTrainingRequest() {
        trainingRequest = TrainingRequest.builder()
                .traineeUsername("")
                .trainerUsername("")
                .trainingName("")
                .trainingDate(LocalDateTime.parse("2020-01-01T10:00:00"))
                .trainingDuration(-10L)
                .build();
    }

    @Given("the system contains predefined training types")
    public void theSystemContainsPredefinedTrainingTypes() {
        // No explicit implementation needed here since the data is initialized in data.sql
    }

    @When("the client sends a GET request to get training types at {string}")
    public void theClientSendsAGETRequestToGetTrainingTypesAt(String endpoint) {
        try {
            var url = baseUrl + endpoint;
            var response = restTemplate.getForEntity(url, TrainingTypeResponse[].class); // Directly map to an array
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            log.error("Error retrieving training types: Status - {}, Body - {}", e.getStatusCode(), e.getResponseBodyAsString());
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("the response contains the list of training types")
    public void theResponseContainsTheListOfTrainingTypes() {
        @SuppressWarnings("unchecked")
        var response = (ResponseEntity<TrainingTypeResponse[]>) testContext.getResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var trainingTypes = response.getBody();
        assert trainingTypes != null;
        assertThat(trainingTypes.length).isGreaterThanOrEqualTo(5); // 5 predefined types in data.sql
    }
}