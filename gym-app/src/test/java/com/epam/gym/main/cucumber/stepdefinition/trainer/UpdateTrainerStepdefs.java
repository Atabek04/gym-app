package com.epam.gym.main.cucumber.stepdefinition.trainer;

import com.epam.gym.main.cucumber.stepdefinition.TestContext;
import com.epam.gym.main.dto.TrainerResponse;
import com.epam.gym.main.dto.TrainerUpdateRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class UpdateTrainerStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private TrainerUpdateRequest trainerPayload;

    @And("a valid trainer update request payload")
    public void aValidTrainerUpdateRequestPayload() {
        trainerPayload = TrainerUpdateRequest.builder()
                .firstName("Dina")
                .lastName("Prince")
                .specialization("YOGA")
                .isActive(true)
                .build();
    }

    @When("the client sends a PUT request to update trainer at {string}")
    public void theClientSendsAPUTRequestTo(String url) {
        var baseUrl = "http://localhost:9876/api/v1";
        var username = testContext.getUsername();
        var response = restTemplate.exchange(
                baseUrl + url + username,
                HttpMethod.PUT,
                new HttpEntity<>(trainerPayload),
                TrainerResponse.class
        );
        testContext.setResponse(response);
    }

    @And("the trainer's details are updated in the system")
    public void theTrainerSDetailsAreUpdatedInTheSystem() {
        @SuppressWarnings("unchecked")
        var response = (ResponseEntity<TrainerResponse>) testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var trainerResponse = response.getBody();
        assert trainerResponse != null;
        assertThat(trainerResponse.firstName()).isEqualTo(trainerPayload.firstName());
        assertThat(trainerResponse.lastName()).isEqualTo(trainerPayload.lastName());
        assertThat(trainerResponse.specialization()).isEqualTo(trainerPayload.specialization());
    }

    @And("an invalid trainer update request payload")
    public void anInvalidTrainerUpdateRequestPayload() {
        trainerPayload = TrainerUpdateRequest.builder()
                .firstName("")
                .lastName("")
                .specialization("")
                .isActive(null)
                .build();
    }

    @And("no updates are made to the trainer in the system")
    public void noUpdatesAreMadeToTheTrainerInTheSystem() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
