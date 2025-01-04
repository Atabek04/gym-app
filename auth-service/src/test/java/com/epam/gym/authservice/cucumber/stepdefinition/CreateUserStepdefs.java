package com.epam.gym.authservice.cucumber.stepdefinition;

import com.epam.gym.authservice.dto.AuthUserDTO;
import com.epam.gym.authservice.model.UserRole;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateUserStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private final String baseUrl = "http://localhost:9876";
    private AuthUserDTO userPayload;

    public CreateUserStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }


    @Given("a valid user payload")
    public void aValidUserPayload() {
        userPayload = AuthUserDTO.builder()
                .username("Abu.Hamza")
                .password("123456")
                .role(UserRole.ROLE_TRAINEE)
                .isActive(true)
                .build();
    }

    @When("the client sends a POST request to {string} with the user payload")
    public void theClientSendsAPOSTRequestTo(String url) {
        var response = restTemplate.postForEntity(baseUrl + url, userPayload, String.class);
        testContext.setResponse(response);
    }


    @And("the user is created in the system")
    public void theUserIsCreatedInTheSystem() {
        var url = baseUrl + "/api/v1/auth/users/exists/" + userPayload.username();
        var isExistsResponse = restTemplate.getForEntity(url, Boolean.class);

        assertThat(isExistsResponse.getStatusCode().value()).isEqualTo(200);
        assertThat(isExistsResponse.getBody()).isTrue();
    }

    @Given("an invalid user payload")
    public void anInvalidUserPayload() {
        userPayload = AuthUserDTO.builder()
                .username("")
                .password("123456")
                .role(null)
                .isActive(null)
                .build();
    }

    @Then("the server responds with a {int} status")
    public void theServerRespondsWithAStatus(int status) {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(status);
    }
}
