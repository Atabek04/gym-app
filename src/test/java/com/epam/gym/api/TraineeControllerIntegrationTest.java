package com.epam.gym.api;

import com.epam.gym.api.parameterResolver.TraineeServiceParameterResolver;
import com.epam.gym.config.ApplicationConfig;
import com.epam.gym.controller.TraineeController;
import com.epam.gym.dto.BasicTrainerResponse;
import com.epam.gym.dto.TraineeRequest;
import com.epam.gym.dto.TraineeResponse;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.exception.GlobalExceptionHandler;
import com.epam.gym.model.Trainee;
import com.epam.gym.model.TrainingType;
import com.epam.gym.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, TraineeServiceParameterResolver.class})
@ContextConfiguration(classes = {ApplicationConfig.class})
@RequiredArgsConstructor
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TraineeControllerIntegrationTest {
    private static MockMvc mockMvc;
    @Mock
    private TraineeService traineeService;
    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(traineeController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Add global exception handler if needed
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) // Handle character encoding
                .build();
    }

    private String getBasicAuthHeader() {
        String credentials = "Man.Super" + ":" + "123";
        byte[] base64Credentials = Base64.getEncoder().encode(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(base64Credentials, StandardCharsets.UTF_8);
    }

    @BeforeAll
    public static void ensureTraineeDoesNotExistBeforeTests(@Mock TraineeService traineeService) {
        MockitoAnnotations.openMocks(TraineeControllerIntegrationTest.class);
        Optional<Trainee> existingTrainee = traineeService.findByUsername("Halid.Ismail");
        if (existingTrainee.isPresent()) {
            traineeService.delete("Halid.Ismail");
        }
    }


    @Test
    @Order(1)
    void givenValidTraineeRequest_whenCreateTrainee_thenStatusCreated() throws Exception {
        var mockUserCredentials = UserCredentials.builder()
                .username("Halid.Ismail")
                .password("MPYJR9hx4n")
                .build();

        String traineeRequestJson = """
                   {
                       "firstName": "Halid",
                       "lastName": "Ismail",
                       "address": "Abu-Dhabi",
                       "dateOfBirth": "1990-01-01"
                   }
                """;

        when(traineeService.create(any(TraineeRequest.class))).thenReturn(mockUserCredentials);

        mockMvc.perform(post("/v1/trainees")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeRequestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void givenMissingRequiredFields_whenCreateTrainee_thenStatusBadRequest() throws Exception {
        String traineeRequestJson = """
                    {
                        "address": "123 Main St",
                        "dateOfBirth": "1990-01-01"
                    }
                """;

        mockMvc.perform(post("/v1/trainees")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void givenValidUsername_whenGetTraineeByUsername_thenReturnTraineeResponse() throws Exception {
        TraineeResponse mockTraineeResponse = TraineeResponse.builder()
                .username("Halid.Ismail")
                .firstName("Halid")
                .lastName("Ismail")
                .build();

        when(traineeService.getTraineeAndTrainers("Halid.Ismail")).thenReturn(mockTraineeResponse);

        mockMvc.perform(get("/v1/trainees/Halid.Ismail")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void givenValidTraineeUpdateRequest_whenUpdateTrainee_thenStatusOk() throws Exception {
        String updateRequestJson = """
                    {
                        "firstName": "Valid",
                        "lastName": "Ibn Mubarak",
                        "address": "Abu-Dhabi",
                        "dateOfBirth": "1990-01-01",
                        "isActive": false
                    }
                """;

        TraineeResponse mockResponse = TraineeResponse.builder()
                .username("Halid.Ismail")
                .firstName("Valid")
                .lastName("Ibn Mubarak")
                .dateOfBirth("1990-01-01")
                .address("Abu-Dhabi")
                .isActive(false)
                .build();

        when(traineeService.updateTraineeAndUser(any(), eq("Halid.Ismail"))).thenReturn(mockResponse);

        mockMvc.perform(put("/v1/trainees/Halid.Ismail")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void givenInvalidUpdateRequest_whenUpdateTrainee_thenStatusBadRequest() throws Exception {
        String invalidUpdateRequestJson = """
                    {
                        "firstName": "V",
                        "lastName": "",
                        "isActive": null
                    }
                """;

        mockMvc.perform(put("/v1/trainees/Halid.Ismail")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUpdateRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void givenValidTrainerUsernames_whenUpdateTrainers_thenStatusOk() throws Exception {
        TraineeResponse mockResponse = TraineeResponse.builder()
                .username("Halid.Ismail")
                .firstName("Halid")
                .lastName("Ismail")
                .trainers(List.of(
                        new BasicTrainerResponse("NonSuper", "Trainer", "Super.Trainer", true, "CROSSFIT"),
                        new BasicTrainerResponse("Ivan", "Urgant", "Ivan.Urgant", true, "YOGA"),
                        new BasicTrainerResponse("Bat", "Man", "Bat.Man", false, "YOGA")
                ))
                .build();

        when(traineeService.getTraineeAndTrainers("Halid.Ismail")).thenReturn(mockResponse);

        mockMvc.perform(put("/v1/trainees/Halid.Ismail/trainers")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Super.Trainer\", \"Ivan.Urgant\", \"Bat.Man\"]"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void givenEmptyTrainerUsernames_whenUpdateTrainers_thenReturnEmptyTrainersArray() throws Exception {
        TraineeResponse mockTraineeResponse = TraineeResponse.builder()
                .username("Halid.Ismail")
                .firstName("Halid")
                .lastName("Ismail")
                .trainers(Collections.emptyList())
                .build();

        when(traineeService.getTraineeAndTrainers("Halid.Ismail")).thenReturn(mockTraineeResponse);

        mockMvc.perform(put("/v1/trainees/Halid.Ismail/trainers")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void givenValidUsername_whenGetNotAssignedTrainers_thenReturnTrainerList() throws Exception {
        List<BasicTrainerResponse> mockTrainers = List.of(
                new BasicTrainerResponse("Grange", "Collum", "Grange.Collum", true, "STRENGTH_TRAINING")
        );

        when(traineeService.getNotAssignedTrainers("Halid.Ismail")).thenReturn(mockTrainers);

        mockMvc.perform(get("/v1/trainees/Halid.Ismail/trainers")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    void givenValidTrainingFilter_whenGetTraineeTrainings_thenReturnTrainingList() throws Exception {
        List<TrainingResponse> mockTrainings = List.of(
                new TrainingResponse(45L, 61L, 48L, "Halid", "Ismail",
                        "NonSuper", "Trainer", "Dummy Training Name",
                        TrainingType.valueOf("CARDIO"), ZonedDateTime.now(), 60L)
        );

        when(traineeService.getTraineeTrainings(eq("Halid.Ismail"), any())).thenReturn(mockTrainings);

        String filterRequestJson = """
                    {
                        "trainingType": "CARDIO"
                    }
                """;

        mockMvc.perform(get("/v1/trainees/Halid.Ismail/trainings")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterRequestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(11)
    void givenValidStatusUpdate_whenUpdateTraineeStatus_thenStatusOk() throws Exception {
        String updateStatusRequestJson = """
                    {
                        "isActive": false
                    }
                """;

        mockMvc.perform(patch("/v1/trainees/Halid.Ismail/status")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateStatusRequestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    void givenValidUsername_whenDeleteTrainee_thenStatusNoContent() throws Exception {
        when(traineeService.findByUsername("Halid.Ismail")).thenReturn(Optional.of(new Trainee()));

        mockMvc.perform(delete("/v1/trainees/Halid.Ismail")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}