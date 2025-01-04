package com.epam.gym.authservice.cucumber.stepdefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CheckUsernameAvailable {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private String username;

    public CheckUsernameAvailable(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @Given("a username {string}")
    public void aUsername(String username) {
        this.username = username;
    }

    @When("the client sends a GET request to {string}")
    public void theClientSendsAGETRequestTo(String url) {
        var baseUrl = "http://localhost:9876";
        var response = restTemplate.getForEntity(baseUrl + url + "/" + username, Boolean.class);
        testContext.setResponse(response);
        
    }

    @And("the response body indicates the username is_taken as true")
    public void theResponseBodyIndicatesTheUsernameIs_takenAsTrue() {
        var response = testContext.getResponse();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat((Boolean) response.getBody()).isTrue();
    }

    @And("the response body indicates the username is_taken as false")
    public void theResponseBodyIndicatesTheUsernameIs_takenAsFalse() {
        var response = testContext.getResponse();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat((Boolean) response.getBody()).isFalse();
    }
}
