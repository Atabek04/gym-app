package com.epam.gym.authservice.cucumber.stepdefinition;

import com.epam.gym.authservice.dto.UserCredentials;
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
public class RefreshTokenStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private String refreshToken;

    public RefreshTokenStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @Given("a valid refresh token")
    public void aValidRefreshToken() {
        // Logging in to get a refresh token
        var credentials = UserCredentials.builder()
                .username("Jack.Sparrow")
                .password("123456")
                .build();
        var response = restTemplate.postForEntity("http://localhost:9876/api/v1/auth/login", credentials, Map.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assert responseBody != null;
        assertThat(responseBody.containsKey("refreshToken")).isTrue();
        refreshToken = responseBody.get("refreshToken");
    }

    @Given("an invalid refresh token")
    public void anInvalidRefreshToken() {
        refreshToken = "invalidRefreshToken";
    }

    @When("the client sends a POST request to {string}")
    public void theClientSendsAPOSTRequestTo(String url) {
        try {
            String baseUrl = "http://localhost:9876";
            var response = restTemplate.postForEntity(baseUrl + url, refreshToken, String.class);
            testContext.setResponse(response);
            log.info("Response: {}", response);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException: {}", e.getResponseBodyAsString());
            testContext.setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
        }
    }

    @And("the response body contains a new access token and refresh token")
    public void theResponseBodyContainsANewAccessTokenAndRefreshToken() {
        var response = testContext.getResponse();
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        var body = (String) response.getBody();
        assert body != null;
        assertThat(body).contains("accessToken");
    }
}
