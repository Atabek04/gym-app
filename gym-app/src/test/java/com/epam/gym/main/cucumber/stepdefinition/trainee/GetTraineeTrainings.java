package com.epam.gym.main.cucumber.stepdefinition.trainee;

import com.epam.gym.main.dto.TrainingResponse;
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
public class GetTraineeTrainings {

    private final RestTemplate restTemplate;
    private final TestContext testContext;

    public GetTraineeTrainings(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @When("the client sends a GET request to get trainee trainings at {string}")
    public void theClientSendsAGETRequestToGetTraineeTrainingsAt(String url) {
        String baseUrl = "http://localhost:9876";
        try {
            var response = restTemplate.exchange(
                    baseUrl + url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TrainingResponse>>() {}
            );
            log.info("Response Body: {}", response.getBody());
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            log.error("Error Response: {}", e.getResponseBodyAsString());
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("all trainee trainings are returned")
    public void allTraineeTrainingsAreReturned() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isInstanceOf(List.class);
        @SuppressWarnings("unchecked")
        var trainings = (List<TrainingResponse>) response.getBody();
        log.info("Trainee Trainings: {}", trainings);

        assertThat(trainings).isNotEmpty();
        trainings.forEach(training -> {
            assertThat(training.traineeFirstName()).isNotNull();
            assertThat(training.trainerFirstName()).isNotNull();
            assertThat(training.trainingName()).isNotNull();
        });
    }
}
