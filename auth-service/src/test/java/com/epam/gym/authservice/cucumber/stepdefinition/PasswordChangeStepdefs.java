package com.epam.gym.authservice.cucumber.stepdefinition;

import com.epam.gym.authservice.dto.UserCredentials;
import com.epam.gym.authservice.dto.UserNewPasswordCredentials;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PasswordChangeStepdefs {

    private final RestTemplate restTemplate;
    private final TestContext testContext;
    private final String baseUrl = "http://localhost:9876";
    private UserNewPasswordCredentials credentials;

    public PasswordChangeStepdefs(RestTemplate restTemplate, TestContext testContext) {
        this.restTemplate = restTemplate;
        this.testContext = testContext;
    }

    @Given("a valid password change request")
    public void aValidPasswordChangeRequest() {
        credentials = UserNewPasswordCredentials.builder()
                .username("Bruce.Wayne")
                .oldPassword("123")
                .newPassword("123456")
                .build();
    }

    @When("the client sends a PUT request to {string}")
    public void theClientSendsAPUTRequestTo(String url) {
        var requestEntity = new HttpEntity<>(credentials);
        var response = restTemplate.exchange(
                baseUrl + url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
        testContext.setResponse(response);
    }

    @And("the password is updated successfully")
    public void thePasswordIsUpdatedSuccessfully() {
        var loginCredentials = UserCredentials.builder()
                .username(credentials.username())
                .password(credentials.newPassword())
                .build();

        var response = restTemplate.postForObject(baseUrl + "/api/v1/auth/login", loginCredentials, String.class);

        assertThat(response).contains("accessToken");
        assertThat(response).contains("refreshToken");
    }

    @Given("an invalid password change request")
    public void anInvalidPasswordChangeRequest() {
        credentials = UserNewPasswordCredentials.builder()
                .username("Bruce.Wayne")
                .oldPassword("1234")
                .newPassword("1234567")
                .build();
    }

    @And("the password is not updated")
    public void thePasswordIsNotUpdated() {
        var loginCredentials = UserCredentials.builder()
                .username(credentials.username())
                .password(credentials.newPassword())
                .build();

        assertThatThrownBy(() -> restTemplate.exchange(
                baseUrl + "/api/v1/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(loginCredentials),
                String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.UNAUTHORIZED);
    }

}
