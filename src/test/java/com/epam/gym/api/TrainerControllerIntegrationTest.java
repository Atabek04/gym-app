package com.epam.gym.api;

import com.epam.gym.GymApp;
import com.epam.gym.api.parameterResolver.TrainerServiceParameterResolver;
import com.epam.gym.controller.TrainerController;
import com.epam.gym.dto.TrainerRequest;
import com.epam.gym.dto.TrainerResponse;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.exception.GlobalExceptionHandler;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.TrainingType;
import com.epam.gym.service.TrainerService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Base64;
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

@ExtendWith({SpringExtension.class, TrainerServiceParameterResolver.class})
@ContextConfiguration(classes = {GymApp.class})
@RequiredArgsConstructor
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TrainerControllerIntegrationTest {

    private static MockMvc mockMvc;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(trainerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    private String getBasicAuthHeader() {
        String credentials = "Bat.Man" + ":" + "123";
        byte[] base64Credentials = Base64.getEncoder().encode(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(base64Credentials, StandardCharsets.UTF_8);
    }

    @BeforeAll
    public static void ensureTrainerDoesNotExistBeforeTests(@Mock TrainerService trainerService) {
        Optional<Trainer> existingTrainer = trainerService.findByUsername("Super.Trainer");
        if (existingTrainer.isPresent()) {
            trainerService.delete("Super.Trainer");
        }
    }

    @Test
    @Order(1)
    void givenValidTrainerRequest_whenCreateTrainer_thenStatusCreated() throws Exception {
        var mockUserCredentials = UserCredentials.builder()
                .username("Super.Trainer")
                .password("m3nheQWg7h")
                .build();

        String trainerRequestJson = """
                {
                    "firstName": "Super",
                    "lastName": "Trainer",
                    "specialization": "CARDIO"
                }
                """;

        when(trainerService.create(any(TrainerRequest.class))).thenReturn(mockUserCredentials);

        mockMvc.perform(post("/v1/trainers")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerRequestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void givenMissingRequiredFields_whenCreateTrainer_thenStatusBadRequest() throws Exception {
        String trainerRequestJson = """
                {
                    "specialization": "CARDIO"
                }
                """;

        mockMvc.perform(post("/v1/trainers")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainerRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void givenValidUsername_whenGetTrainerByUsername_thenReturnTrainerResponse() throws Exception {
        TrainerResponse mockTrainerResponse = TrainerResponse.builder()
                .username("Super.Trainer")
                .firstName("Super")
                .lastName("Trainer")
                .specialization("CARDIO")
                .isActive(true)
                .build();

        when(trainerService.getTrainerAndTrainees("Super.Trainer")).thenReturn(mockTrainerResponse);

        mockMvc.perform(get("/v1/trainers/Super.Trainer")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void givenValidTrainerUpdateRequest_whenUpdateTrainer_thenStatusOk() throws Exception {
        String updateRequestJson = """
                {
                    "firstName": "NonSuper",
                    "lastName": "Trainer",
                    "specialization": "CROSSFIT",
                    "isActive": false
                }
                """;

        TrainerResponse mockResponse = TrainerResponse.builder()
                .username("Super.Trainer")
                .firstName("NonSuper")
                .lastName("Trainer")
                .specialization("CROSSFIT")
                .isActive(false)
                .build();

        when(trainerService.updateTrainerAndUser(any(), eq("Super.Trainer"))).thenReturn(mockResponse);

        mockMvc.perform(put("/v1/trainers/Super.Trainer")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void givenInvalidUpdateRequest_whenUpdateTrainer_thenStatusBadRequest() throws Exception {
        String invalidUpdateRequestJson = """
                {
                    "firstName": "",
                    "lastName": "",
                    "specialization": "CROSSFIT",
                    "isActive": null
                }
                """;

        mockMvc.perform(put("/v1/trainers/Super.Trainer")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUpdateRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void givenValidTrainingFilter_whenGetTrainerTrainings_thenReturnTrainingList() throws Exception {
        List<TrainingResponse> mockTrainings = List.of(
                new TrainingResponse(45L, 61L, 48L, "Halid", "Ismail",
                        "NonSuper", "Trainer", "Dummy Training Name",
                        TrainingType.valueOf("CARDIO"), ZonedDateTime.now(), 60L)
        );

        when(trainerService.findTrainerTrainings(eq("Super.Trainer"), any())).thenReturn(mockTrainings);

        String filterRequestJson = """
                {
                    "traineeName": "Halid"
                }
                """;

        mockMvc.perform(get("/v1/trainers/Super.Trainer/trainings")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterRequestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void givenValidStatusUpdate_whenUpdateTrainerStatus_thenStatusOk() throws Exception {
        String updateStatusRequestJson = """
                {
                    "isActive": true
                }
                """;

        mockMvc.perform(patch("/v1/trainers/Super.Trainer/status")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateStatusRequestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void givenValidUsername_whenDeleteTrainer_thenStatusNoContent() throws Exception {
        when(trainerService.findByUsername("Super.Trainer")).thenReturn(Optional.of(new Trainer()));

        mockMvc.perform(delete("/v1/trainers/Super.Trainer")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}