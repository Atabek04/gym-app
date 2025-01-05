package com.epam.gym.main.cucumber.stepdefinition;

import com.epam.gym.main.dto.TraineeResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class DeleteTraineeStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private String username;

    public DeleteTraineeStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @Given("an existing trainee username for deletion {string}")
    public void anExistingTraineeUsernameForDeletion(String username) {
        this.username = username;
    }

    @When("the client sends a DELETE request to {string}")
    public void theClientSendsADELETERequestTo(String url) {
        String baseUrl = "http://localhost:9876";
        try {
            var response = restTemplate.exchange(
                    baseUrl + url,
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("the trainee is removed from the system")
    public void theTraineeIsRemovedFromTheSystem() {
        String baseUrl = "http://localhost:9876";
        try {
            var response = restTemplate.getForEntity(baseUrl + "/api/v1/trainees/" + username, TraineeResponse.class);
            assertThat(response.getStatusCode().value()).isEqualTo(404);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Error Response: {}", e.getResponseBodyAsString());
            assertThat(e.getStatusCode().value()).isEqualTo(404);
        }
    }
}
