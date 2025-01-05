package com.epam.gym.authservice.cucumber.stepdefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteUserStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private String username;
    private final String baseUrl = "http://localhost:9876";

    public DeleteUserStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }


    @Given("an existing username {string}")
    public void anExistingUsername(String username) {
        this.username = username; // user with this username should exist after sql initialization
    }

    @When("the client sends a DELETE request to {string}")
    public void theClientSendsADELETERequestTo(String url) {
        var response = restTemplate.exchange(baseUrl + url, HttpMethod.DELETE, null, String.class);
        testContext.setResponse(response);
    }


    @And("the user is removed from the system")
    public void theUserIsRemovedFromTheSystem() {
        var checkUrl = baseUrl + "/api/v1/auth/users/exists/" + username;
        var existsResponse = restTemplate.getForEntity(checkUrl, Boolean.class);

        assertThat(existsResponse.getStatusCode().value()).isEqualTo(200);
        assertThat(existsResponse.getBody()).isFalse();
    }

    @Given("a non-existing username {string}")
    public void aNonExistingUsername(String username) {
        this.username = username;

        var url = baseUrl + "/api/v1/auth/users/exists/" + username;
        var existsResponse = restTemplate.getForEntity(url, Boolean.class);
        assertThat(existsResponse.getBody()).isFalse();
    }
}
