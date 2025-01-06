package com.epam.gym.main.cucumber.stepdefinition.trainee;

import com.epam.gym.main.cucumber.stepdefinition.TestContext;
import com.epam.gym.main.dto.TraineeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class GetTraineeStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private String username;

    public GetTraineeStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @Given("an existing trainee username {string}")
    public void anExistingTraineeUsername(String username) {
        this.username = username;
    }

    @Given("a non-existing trainee username {string}")
    public void aNonExistingTraineeUsername(String username) {
        this.username = username;
    }

    @When("the client sends a GET request to {string}")
    public void theClientSendsAGETRequestTo(String url) throws JsonProcessingException {
        String baseUrl = "http://localhost:9876";
        try {
            var response = restTemplate.getForEntity(baseUrl + url + username, TraineeResponse.class);
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            Map<String, String> errorResponse = new ObjectMapper().readValue(
                    e.getResponseBodyAsString(), new TypeReference<>() {}
            );
            testContext.setResponse(new ResponseEntity<>(errorResponse, e.getStatusCode()));
        }
    }

    @And("the trainee details are returned")
    public void theTraineeDetailsAreReturned() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var body = (TraineeResponse) response.getBody();
        assert body != null;
        assertThat(body.username()).isEqualTo(username);
        assertThat(body.firstName()).isEqualTo("Tony");
        assertThat(body.lastName()).isEqualTo("Stark");
        assertThat(body.dateOfBirth()).isEqualTo("1980-05-29");
        assertThat(body.address()).isEqualTo("Malibu, California");
    }
}
