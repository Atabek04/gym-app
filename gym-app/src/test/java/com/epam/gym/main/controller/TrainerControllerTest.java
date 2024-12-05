package com.epam.gym.main.controller;


import com.epam.gym.main.dto.TrainerRequest;
import com.epam.gym.main.dto.TrainerResponse;
import com.epam.gym.main.dto.TrainerTrainingFilterRequest;
import com.epam.gym.main.dto.TrainerUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.model.TrainingType;
import com.epam.gym.main.service.TrainerService;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrainerController.class)
@AutoConfigureMockMvc
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void createTrainer_WithValidRequest_ShouldReturnCreated() throws Exception {
        var request = TrainerRequest.builder()
                .firstName("Super")
                .lastName("Trainer")
                .specialization("CARDIO")
                .build();

        var userCredentials = UserCredentials.builder()
                .username("Super.Trainer")
                .password("123")
                .build();

        when(trainerService.create(request)).thenReturn(userCredentials);

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Super.Trainer"))
                .andExpect(jsonPath("$.password").value("123"));

        verify(trainerService, times(1)).create(request);
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainerResponse() throws Exception {
        var response = TrainerResponse.builder()
                .username("Super.Trainer")
                .firstName("Super")
                .lastName("Trainer")
                .specialization("CARDIO")
                .isActive(true)
                .trainees(Collections.emptyList())
                .build();

        when(trainerService.getTrainerAndTrainees("Super.Trainer")).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainers/{username}", "Super.Trainer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Super.Trainer"))
                .andExpect(jsonPath("$.firstName").value("Super"))
                .andExpect(jsonPath("$.specialization").value("CARDIO"));

        verify(trainerService, times(1)).getTrainerAndTrainees("Super.Trainer");
    }

    @Test
    void updateTrainer_WithValidRequest_ShouldReturnUpdatedTrainer() throws Exception {
        var request = TrainerUpdateRequest.builder()
                .firstName("Super")
                .lastName("CoolTrainer")
                .specialization("YOGA")
                .isActive(false)
                .build();

        var response = TrainerResponse.builder()
                .username("Super.Trainer")
                .firstName("Super")
                .lastName("CoolTrainer")
                .specialization("YOGA")
                .isActive(false)
                .trainees(Collections.emptyList())
                .build();

        when(trainerService.updateTrainerAndUser(request, "Super.Trainer")).thenReturn(response);

        mockMvc.perform(put("/api/v1/trainers/{username}", "Super.Trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Super.Trainer"))
                .andExpect(jsonPath("$.specialization").value("YOGA"))
                .andExpect(jsonPath("$.isActive").value(false));

        verify(trainerService, times(1)).updateTrainerAndUser(request, "Super.Trainer");
    }

    @Test
    void deleteTrainer_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/trainers/{username}", "Super.Trainer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(trainerService, times(1)).delete("Super.Trainer");
    }

    @Test
    void getTrainerTrainings_ShouldReturnTrainingList() throws Exception {
        var trainingList = List.of(
                TrainingResponse.builder()
                        .id(1L)
                        .traineeId(2L)
                        .trainerId(3L)
                        .traineeFirstName("Trainee")
                        .traineeLastName("One")
                        .trainerFirstName("Super")
                        .trainerLastName("Trainer")
                        .trainingName("Cardio Session")
                        .trainingType(TrainingType.CARDIO)
                        .trainingDate(LocalDateTime.of(2023, 12, 1, 10, 0))
                        .trainingDuration(60L)
                        .build()
        );

        var filterRequest = TrainerTrainingFilterRequest.builder()
                .periodFrom(LocalDateTime.of(2023, 12, 1, 0, 0))
                .periodTo(LocalDateTime.of(2023, 12, 31, 23, 59))
                .build();

        when(trainerService.findTrainerTrainings("Super.Trainer", filterRequest)).thenReturn(trainingList);

        mockMvc.perform(get("/api/v1/trainers/{username}/trainings", "Super.Trainer")
                        .param("periodFrom", "2023-12-01T00:00:00")
                        .param("periodTo", "2023-12-31T23:59:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName").value("Cardio Session"))
                .andExpect(jsonPath("$[0].trainingType").value("CARDIO"));

        verify(trainerService, times(1)).findTrainerTrainings(eq("Super.Trainer"), any());
    }
}
