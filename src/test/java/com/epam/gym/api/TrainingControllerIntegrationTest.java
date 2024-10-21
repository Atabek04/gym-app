package com.epam.gym.api;

import com.epam.gym.GymApp;
import com.epam.gym.controller.TrainingController;
import com.epam.gym.dto.TrainingTypeResponse;
import com.epam.gym.exception.GlobalExceptionHandler;
import com.epam.gym.model.TrainingType;
import com.epam.gym.service.TrainingService;
import lombok.RequiredArgsConstructor;
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
import java.util.Base64;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GymApp.class})
@RequiredArgsConstructor
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TrainingControllerIntegrationTest {

    private static MockMvc mockMvc;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    private String getBasicAuthHeader() {
        String credentials = "Bat.Man" + ":" + "123";
        byte[] base64Credentials = Base64.getEncoder().encode(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(base64Credentials, StandardCharsets.UTF_8);
    }

    @Test
    @Order(1)
    void givenValidTrainingRequest_whenCreateTraining_thenStatusCreated() throws Exception {
        String trainingRequestJson = """
                {
                  "traineeUsername": "Man.Super",
                  "trainerUsername": "Otabek.Shadimatov",
                  "trainingName": "Cardio Session",
                  "trainingDate": "2023-10-01T10:00:00Z",
                  "trainingDuration": 60
                }
                """;


        mockMvc.perform(post("/v1/trainings")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingRequestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void givenValidTrainingTypesRequest_whenListAllTrainingTypes_thenReturnTrainingTypes() throws Exception {
        List<TrainingTypeResponse> mockTrainingTypes = List.of(
                new TrainingTypeResponse(1, TrainingType.CARDIO_TRAINING),
                new TrainingTypeResponse(2, TrainingType.CIRCUIT_TRAINING),
                new TrainingTypeResponse(3, TrainingType.YOGA)
        );

        when(trainingService.getAllTrainingTypes()).thenReturn(mockTrainingTypes);

        mockMvc.perform(get("/v1/trainings")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void givenInvalidTrainingRequest_whenCreateTraining_thenStatusBadRequest() throws Exception {
        // Missing required field "trainerUsername"
        String invalidTrainingRequestJson = """
                {
                  "traineeUsername": "Man.Super",
                  "trainingName": "Cardio Session",
                  "trainingDate": "2023-10-01T10:00:00Z",
                  "trainingDuration": 60
                }
                """;

        mockMvc.perform(post("/v1/trainings")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTrainingRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    void givenEmptyTrainingTypes_whenListAllTrainingTypes_thenReturnEmptyArray() throws Exception {
        when(trainingService.getAllTrainingTypes()).thenReturn(List.of());

        mockMvc.perform(get("/v1/trainings")
                        .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}