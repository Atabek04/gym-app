package com.epam.gym.main.cucumber.stepdefinition.trainee;

import com.epam.gym.main.dto.BasicTrainerResponse;
import com.epam.gym.main.dto.TraineeResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class UpdateTraineeTrainersStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private List<String> trainerUsernames;
    private final String baseUrl = "http://localhost:9876";

    public UpdateTraineeTrainersStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @And("a list of trainer usernames [{string}, {string}]")
    public void aListOfTrainerUsernames(String trainer1, String trainer2) {
        trainerUsernames = List.of(trainer1, trainer2);
    }

    @And("an invalid list of trainer usernames [{string}]")
    public void anInvalidListOfTrainerUsernames(String trainer) {
        trainerUsernames = List.of(trainer);
    }

    @When("the client sends a PUT request to update trainers at {string}")
    public void theClientSendsAPUTRequestTo(String url) {
        try {
            var response = restTemplate.exchange(
                    baseUrl + url,
                    org.springframework.http.HttpMethod.PUT,
                    new org.springframework.http.HttpEntity<>(trainerUsernames),
                    TraineeResponse.class
            );
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("the updated list of trainers is returned")
    public void theUpdatedListOfTrainersIsReturned() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var body = (TraineeResponse) response.getBody();
        assert body != null;
        assertThat(body.trainers()).isNotNull();
        assertThat(body.trainers().size()).isEqualTo(trainerUsernames.size());
        assertThat(body.trainers())
                .extracting(BasicTrainerResponse::username)
                .containsAll(trainerUsernames);
    }

    @When("the client sends a GET request to get not assigned trainers at {string}")
    public void theClientSendsAGETRequestToGetNotAssignedTrainersAt(String url) {
        try {
            var response = restTemplate.exchange(
                    baseUrl + url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BasicTrainerResponse>>() {}
            );
            log.info("Response Body: {}", response.getBody());
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            log.error("Error Response: {}", e.getResponseBodyAsString());
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("the list of not assigned trainers is returned")
    public void theListOfNotAssignedTrainersIsReturned() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var body = response.getBody();
        assert body != null;

        assertThat(body).isInstanceOf(List.class);
        @SuppressWarnings("unchecked")
        var trainers = (List<BasicTrainerResponse>) body;

        log.info("Not assigned trainers: {}", trainers);

        assertThat(trainers).isNotEmpty();
        assertThat(trainers.get(0)).isInstanceOf(BasicTrainerResponse.class);
    }
}
