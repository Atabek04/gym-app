package com.epam.gym.main.cucumber.stepdefinition;

import com.epam.gym.main.dto.TraineeResponse;
import com.epam.gym.main.dto.TraineeUpdateRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UpdateTraineeStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private TraineeUpdateRequest traineeUpdateRequest;

    public UpdateTraineeStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @Given("a valid trainee update request")
    public void aValidTraineeUpdateRequest() {
        traineeUpdateRequest = TraineeUpdateRequest.builder()
                .firstName("Bruce")
                .lastName("Wayne")
                .address("Updated Gotham City")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .isActive(true)
                .build();
    }

    @Given("an invalid trainee update request")
    public void anInvalidTraineeUpdateRequest() {
        traineeUpdateRequest = TraineeUpdateRequest.builder()
                .firstName("")
                .lastName("W")
                .address("Invalid Address")
                .dateOfBirth(LocalDate.of(2025, 1, 1))
                .isActive(null)
                .build();
    }

    @When("the client sends a PUT request to update the trainee at {string}")
    public void theClientSendsAPUTRequestTo(String url) {
        String baseUrl = "http://localhost:9876";
        try {
            var response = restTemplate.exchange(
                    baseUrl + url,
                    HttpMethod.PUT,
                    new HttpEntity<>(traineeUpdateRequest),
                    TraineeResponse.class
            );
            log.info("Response Body: {}", response.getBody());
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            log.error("Error Response: {}", e.getResponseBodyAsString());
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("the updated trainee details are returned")
    public void theUpdatedTraineeDetailsAreReturned() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var body = (TraineeResponse) response.getBody();
        assert body != null;
        assertThat(body.firstName()).isEqualTo(traineeUpdateRequest.firstName());
        assertThat(body.lastName()).isEqualTo(traineeUpdateRequest.lastName());
        assertThat(body.address()).isEqualTo(traineeUpdateRequest.address());
        assertThat(body.dateOfBirth()).isEqualTo(traineeUpdateRequest.dateOfBirth().toString());
    }
}