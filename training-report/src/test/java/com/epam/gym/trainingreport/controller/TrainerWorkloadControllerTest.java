package com.epam.gym.trainingreport.controller;

import com.epam.gym.trainingreport.dto.TrainerWorkloadRequest;
import com.epam.gym.trainingreport.exception.TrainerWorkloadNotFoundException;
import com.epam.gym.trainingreport.model.ActionType;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

        verify(service, times(1)).createTrainerWorkload(request);
    }

    @Test
    void getTrainerSummary_WithNonExistingUsername_ShouldReturnNotFound() throws Exception {
        when(service.getTrainerWorkload("NonExistent.Trainer"))
                .thenThrow(new TrainerWorkloadNotFoundException("Trainer workload not found"));

        mockMvc.perform(get("/api/v1/workload/{username}", "NonExistent.Trainer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Trainer workload not found"));

        verify(service, times(1)).getTrainerWorkload("NonExistent.Trainer");
    }
}
