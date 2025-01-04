package com.epam.gym.authservice.cucumber.stepdefinition;

import com.epam.gym.authservice.dto.UserCredentials;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.web.client.RestTemplate;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private UserCredentials credentials;

    public LoginStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @Given("valid credentials")
    public void validCredentials() {
        credentials = UserCredentials.builder()
                .username("Jack.Sparrow")
                .password("123456")
                .build();
    }

    @Given("invalid credentials")
    public void invalidCredentials() {
        credentials = UserCredentials.builder()
                .username("invalidUser")
                .password("wrongPassword")
                .build();
    }

    @When("the client sends a POST request to {string} with the login payload")
    public void theClientSendsAPOSTRequestTo(String url) {
        String baseUrl = "http://localhost:9876";
        var response = restTemplate.postForEntity(baseUrl + url, credentials, String.class);
        testContext.setResponse(response);
    }

    @And("the response body contains a valid access token and refresh token")
    public void theResponseBodyContainsAValidAccessTokenAndRefreshToken() {
        var response = testContext.getResponse();

        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var body = (String) response.getBody();
        assertThat(body).isNotBlank();
        assertThat(body).contains("accessToken").contains("refreshToken");
    }
}
