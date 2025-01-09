package com.epam.gym.trainingreport.cucumber.stepdefs;

import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.dto.TrainerWorkloadResponse;
import com.epam.gym.trainingreport.model.ActionType;
import com.epam.gym.trainingreport.model.TrainerWorkload;
import com.epam.gym.trainingreport.model.TrainingDuration;
import com.epam.gym.trainingreport.model.TrainingYear;
import com.epam.gym.trainingreport.repository.TrainerWorkloadRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadStepdefs {

    private final RestTemplate restTemplate;
    private final TrainerWorkloadRepository trainerWorkloadRepository;

    private TrainerWorkloadRequest workloadRequest;
    private ResponseEntity<?> response;
    private final String baseUrl = "http://localhost:9876/api/v1/workload";

    @Before
    public void setup() {
        trainerWorkloadRepository.deleteAll();

        TrainerWorkload workload = TrainerWorkload.builder()
                .id("1")
                .username("Peter.Parker")
                .firstName("Peter")
                .lastName("Parker")
                .isActive(true)
                .yearlySummary(List.of(
                        TrainingYear.builder()
                                .year(2023)
                                .trainingDurations(List.of(
                                        TrainingDuration.builder()
                                                .month(1)
                                                .totalDuration(120)
                                                .build(),
                                        TrainingDuration.builder()
                                                .month(2)
                                                .totalDuration(100)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        trainerWorkloadRepository.save(workload);
        log.info("Data initialization of TrainerWorkload collection is completed");
    }

    @Given("a valid trainer workload request")
    public void aValidTrainerWorkloadRequest() {
        workloadRequest = TrainerWorkloadRequest.builder()
                .username("Peter.Parker")
                .firstName("Peter")
                .lastName("Parker")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(120)
                .actionType(ActionType.ADD)
                .build();
    }

    @Given("an invalid trainer workload request")
    public void anInvalidTrainerWorkloadRequest() {
        workloadRequest = TrainerWorkloadRequest.builder()
                .username("")
                .firstName("")
                .lastName("")
                .isActive(null)
                .trainingDate(LocalDate.now().minusDays(1))
                .trainingDuration(-10)
                .actionType(null)
                .build();
    }

    @When("the client sends a POST request to {string}")
    public void theClientSendsAPOSTRequestTo(String endpoint) {
        try {
            response = restTemplate.postForEntity(baseUrl + endpoint, workloadRequest, Void.class);
        } catch (HttpClientErrorException e) {
            response = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Then("the server responds with a {int} status")
    public void theServerRespondsWithAStatus(int expectedStatus) {
        assertThat(response.getStatusCode().value()).isEqualTo(expectedStatus);
    }

    @And("the trainer workload is successfully created")
    public void theTrainerWorkloadIsSuccessfullyCreated() {
        Optional<TrainerWorkload> workload = trainerWorkloadRepository.findByUsername("Peter.Parker");
        assertThat(workload).isPresent();
        assertThat(workload.get().getFirstName()).isEqualTo("Peter");
        assertThat(workload.get().getYearlySummary()).isNotEmpty();
    }

    @Given("an existing trainer username {string}")
    public void anExistingTrainerUsername(String username) {
        assertThat(trainerWorkloadRepository.findByUsername(username)).isPresent();
    }

    @When("the client sends a GET request to {string}")
    public void theClientSendsAGETRequestTo(String endpoint) {
        try {
            response = restTemplate.getForEntity(baseUrl + endpoint, TrainerWorkloadResponse.class);
        } catch (HttpClientErrorException e) {
            response = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @And("the response contains the trainer's workload summary")
    public void theResponseContainsTheTrainerSWorkloadSummary() {
        TrainerWorkloadResponse workloadResponse = (TrainerWorkloadResponse) response.getBody();
        assertThat(workloadResponse).isNotNull();
        assertThat(workloadResponse.username()).isEqualTo("Peter.Parker");
        assertThat(workloadResponse.yearlySummary()).isNotEmpty();
    }

    @Given("a non-existing trainer username {string}")
    public void aNonExistingTrainerUsername(String username) {
        assertThat(trainerWorkloadRepository.findByUsername(username)).isNotPresent();
    }
}