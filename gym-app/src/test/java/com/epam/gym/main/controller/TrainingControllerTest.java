package com.epam.gym.main.controller;

import com.epam.gym.main.dto.TrainingRequest;
import com.epam.gym.main.dto.TrainingTypeResponse;
import com.epam.gym.main.model.TrainingType;
import com.epam.gym.main.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrainingController.class)
@AutoConfigureMockMvc
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void createTraining_WithValidRequest_ShouldReturnCreated() throws Exception {
        var request = TrainingRequest.builder()
                .traineeUsername("Super.Trainee")
                .trainerUsername("Super.Trainer")
                .trainingName("Super Cool Training Name")
                .trainingDate(LocalDateTime.of(2025, 1, 1, 14, 0))
                .trainingDuration(120L)
                .build();

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(trainingService, times(1)).create(request);
    }

    @Test
    void createTraining_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        var request = TrainingRequest.builder()
                .traineeUsername("")  // Invalid: Blank trainee username
                .trainerUsername("Super.Trainer")
                .trainingName("Super Cool Training Name")
                .trainingDate(LocalDateTime.of(2025, 1, 1, 14, 0))
                .trainingDuration(120L)
                .build();

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(trainingService, times(0)).create(request);
    }

    @Test
    void listAllTrainingTypes_ShouldReturnTrainingTypeList() throws Exception {
        var trainingTypes = List.of(
                TrainingTypeResponse.builder().trainingTypeId(1).trainingType(TrainingType.CARDIO).build(),
                TrainingTypeResponse.builder().trainingTypeId(2).trainingType(TrainingType.YOGA).build()
        );

        when(trainingService.getAllTrainingTypes()).thenReturn(trainingTypes);

        mockMvc.perform(get("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingTypeId").value(1))
                .andExpect(jsonPath("$[0].trainingType").value("CARDIO"))
                .andExpect(jsonPath("$[1].trainingTypeId").value(2))
                .andExpect(jsonPath("$[1].trainingType").value("YOGA"));

        verify(trainingService, times(1)).getAllTrainingTypes();
    }
}
