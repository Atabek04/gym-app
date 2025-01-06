package com.epam.gym.main.cucumber.stepdefinition.trainee;

import com.epam.gym.main.cucumber.stepdefinition.TestContext;
import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.UserCredentials;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class CreateTraineeStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private TraineeRequest traineePayload;

    public CreateTraineeStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @Given("a valid trainee request")
    public void aValidTraineeRequest() {
        traineePayload = TraineeRequest.builder()
                .firstName("Abu")
                .lastName("Yusuf")
                .address("Bagdad 12")
                .dateOfBirth(LocalDate.of(729, 1, 1))
                .build();
    }

    @Given("an invalid trainee request")
    public void anInvalidTraineeRequest() {
        traineePayload = TraineeRequest.builder()
                .firstName("")
                .lastName("")
                .address(null)
                .dateOfBirth(LocalDate.now())
                .build();
    }

    @When("the client sends a POST request to {string}")
    public void theClientSendsAPOSTRequestTo(String url) {
        try {
            String baseUrl = "http://localhost:9876";
            log.info("Sending POST request to {}", baseUrl + url);
            var response = restTemplate.postForEntity(baseUrl + url, traineePayload, UserCredentials.class);
            testContext.setResponse(response);
            log.info("Successful Response: Status - {}, Body - {}", response.getStatusCode(), response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Error response status: {}", e.getStatusCode());
            log.error("Error response body: {}", e.getResponseBodyAsString());
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @Then("the server responds with a {int} status")
    public void theServerRespondsWithAStatus(int status) {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(status);
    }

    @And("the trainee credentials are returned")
    public void theTraineeCredentialsAreReturned() {
        var response = testContext.getResponse();

        assertThat(response.getBody()).isInstanceOf(UserCredentials.class);

        var credentials = (UserCredentials) response.getBody();
        assert credentials != null;
        assertThat(credentials.username()).isNotBlank();
        assertThat(credentials.password()).isNotBlank();
    }
}
