package com.epam.gym.trainingreport.controller;

import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.dto.TrainerWorkloadResponse;
import com.epam.gym.trainingreport.exception.TrainerWorkloadNotFoundException;
import com.epam.gym.trainingreport.model.ActionType;
import com.epam.gym.trainingreport.model.TrainingDuration;
import com.epam.gym.trainingreport.model.TrainingYear;
import com.epam.gym.trainingreport.service.TrainerWorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = TrainerWorkloadController.class)
@AutoConfigureMockMvc
class TrainerWorkloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TrainerWorkloadService service;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void handleTraining_WithValidRequest_ShouldReturnOk() throws Exception {
        var request = TrainerWorkloadRequest.builder()
                .username("Super.Trainer")
                .firstName("Super")
                .lastName("Trainer")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 1, 1))
                .trainingDuration(60)
                .actionType(ActionType.ADD)
                .build();

        mockMvc.perform(post("/api/v1/workload/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service, times(1)).processTraining(request);
    }

    @Test
    void getTrainerSummary_WithExistingUsername_ShouldReturnTrainerWorkloadResponse() throws Exception {
        var trainingDurations = List.of(
                new TrainingDuration(1, 120, null),
                new TrainingDuration(2, 180, null)
        );

        var trainingYear = new TrainingYear();
        trainingYear.setYear(2024);
        trainingYear.setTrainingDurations(trainingDurations);

        var response = TrainerWorkloadResponse.builder()
                .username("Super.Trainer")
                .yearlySummary(List.of(trainingYear))
                .build();

        when(service.getTrainerSummary("Super.Trainer")).thenReturn(response);

        mockMvc.perform(get("/api/v1/workload/{username}", "Super.Trainer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Super.Trainer"))
                .andExpect(jsonPath("$.years[0].year").value(2024))
                .andExpect(jsonPath("$.years[0].months[0].month").value(1))
                .andExpect(jsonPath("$.years[0].months[0].totalDuration").value(120));

        verify(service, times(1)).getTrainerSummary("Super.Trainer");
    }

    @Test
    void getTrainerSummary_WithNonExistingUsername_ShouldReturnNotFound() throws Exception {
        when(service.getTrainerSummary("NonExistent.Trainer"))
                .thenThrow(new TrainerWorkloadNotFoundException("Trainer workload not found"));

        mockMvc.perform(get("/api/v1/workload/{username}", "NonExistent.Trainer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Trainer workload not found"));

        verify(service, times(1)).getTrainerSummary("NonExistent.Trainer");
    }
}
