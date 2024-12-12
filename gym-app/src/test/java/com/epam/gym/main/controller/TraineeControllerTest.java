package com.epam.gym.main.controller;


import com.epam.gym.main.dto.BasicTrainerResponse;
import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.TraineeResponse;
import com.epam.gym.main.dto.TraineeTrainingFilterRequest;
import com.epam.gym.main.dto.TraineeUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.model.TrainingType;
import com.epam.gym.main.service.TraineeService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TraineeController.class)
@AutoConfigureMockMvc
class TraineeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void createTrainee_WithValidRequest_ShouldReturnCreated() throws Exception {
        var request = TraineeRequest.builder()
                .firstName("Abu")
                .lastName("Yusuf")
                .address("Bagdad 12")
                .dateOfBirth(LocalDate.of(729, 1, 1))
                .build();

        var userCredentials = UserCredentials.builder()
                .username("Super.Trainee")
                .password("123")
                .build();

        when(traineeService.create(request)).thenReturn(userCredentials);

        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Super.Trainee"))
                .andExpect(jsonPath("$.password").value("123"));

        verify(traineeService, times(1)).create(request);
    }

    @Test
    void getTraineeByUsername_ShouldReturnTraineeResponse() throws Exception {
        var response = TraineeResponse.builder()
                .username("Super.Trainee")
                .firstName("Abu")
                .lastName("Yusuf")
                .dateOfBirth("729-01-01")
                .address("Bagdad 12")
                .isActive(true)
                .trainers(Collections.emptyList())
                .build();

        when(traineeService.getTraineeAndTrainers("Super.Trainee")).thenReturn(response);

        mockMvc.perform(get("/api/v1/trainees/{username}", "Super.Trainee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Super.Trainee"))
                .andExpect(jsonPath("$.firstName").value("Abu"))
                .andExpect(jsonPath("$.lastName").value("Yusuf"));

        verify(traineeService, times(1)).getTraineeAndTrainers("Super.Trainee");
    }

    @Test
    void updateTrainee_WithValidRequest_ShouldReturnUpdatedTrainee() throws Exception {
        var request = TraineeUpdateRequest.builder()
                .firstName("Abu")
                .lastName("Yusuf")
                .address("Bagdad 101")
                .dateOfBirth(LocalDate.of(729, 12, 1))
                .isActive(false)
                .build();

        var response = TraineeResponse.builder()
                .username("Super.Trainee")
                .firstName("Abu")
                .lastName("Yusuf")
                .dateOfBirth("729-12-01")
                .address("Bagdad 101")
                .isActive(false)
                .trainers(Collections.emptyList())
                .build();

        when(traineeService.updateTraineeAndUser(request, "Super.Trainee")).thenReturn(response);

        mockMvc.perform(put("/api/v1/trainees/{username}", "Super.Trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Super.Trainee"))
                .andExpect(jsonPath("$.address").value("Bagdad 101"))
                .andExpect(jsonPath("$.isActive").value(false));

        verify(traineeService, times(1)).updateTraineeAndUser(request, "Super.Trainee");
    }

    @Test
    void deleteTrainee_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/trainees/{username}", "Super.Trainee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(traineeService, times(1)).delete("Super.Trainee");
    }

    @Test
    void updateTrainers_ShouldReturnUpdatedTraineeWithTrainers() throws Exception {
        var trainerUsernames = List.of("Trainer1", "Trainer2");
        var response = TraineeResponse.builder()
                .username("Super.Trainee")
                .firstName("Abu")
                .lastName("Yusuf")
                .trainers(List.of(
                        BasicTrainerResponse.builder()
                                .firstName("Trainer1")
                                .lastName("Smith")
                                .username("Trainer1")
                                .isActive(true)
                                .specialization("Strength Training")
                                .build(),
                        BasicTrainerResponse.builder()
                                .firstName("Trainer2")
                                .lastName("Johnson")
                                .username("Trainer2")
                                .isActive(true)
                                .specialization("Cardio")
                                .build()))
                .build();

        when(traineeService.getTraineeAndTrainers("Super.Trainee")).thenReturn(response);

        mockMvc.perform(put("/api/v1/trainees/{username}/trainers", "Super.Trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(trainerUsernames)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainers[0].firstName").value("Trainer1"))
                .andExpect(jsonPath("$.trainers[1].firstName").value("Trainer2"));

        verify(traineeService, times(1)).updateTrainers("Super.Trainee", trainerUsernames);
        verify(traineeService, times(1)).getTraineeAndTrainers("Super.Trainee");
    }

    @Test
    void getNotAssignedTrainers_ShouldReturnTrainerList() throws Exception {
        var trainers = List.of(
                BasicTrainerResponse.builder()
                        .firstName("Trainer3")
                        .lastName("Lee")
                        .username("Trainer3")
                        .isActive(true)
                        .specialization("Yoga")
                        .build());

        when(traineeService.getNotAssignedTrainers("Super.Trainee")).thenReturn(trainers);

        mockMvc.perform(get("/api/v1/trainees/{username}/trainers", "Super.Trainee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Trainer3"))
                .andExpect(jsonPath("$[0].specialization").value("Yoga"));

        verify(traineeService, times(1)).getNotAssignedTrainers("Super.Trainee");
    }

    @Test
    void getTraineeTrainings_ShouldReturnTrainingList() throws Exception {
        var trainingList = List.of(
                TrainingResponse.builder()
                        .id(1L)
                        .traineeId(1L)
                        .trainerId(2L)
                        .traineeFirstName("Abu")
                        .traineeLastName("Yusuf")
                        .trainerFirstName("Trainer")
                        .trainerLastName("Smith")
                        .trainingName("Cardio Session")
                        .trainingType(TrainingType.CARDIO)
                        .trainingDate(LocalDateTime.of(2023, 12, 1, 10, 0))
                        .trainingDuration(60L)
                        .build());

        var filterRequest = TraineeTrainingFilterRequest.builder()
                .periodFrom(LocalDateTime.of(2023, 12, 1, 0, 0))
                .periodTo(LocalDateTime.of(2023, 12, 31, 23, 59))
                .trainerName("Trainer Smith")
                .trainingType(TrainingType.CARDIO)
                .build();

        when(traineeService.getTraineeTrainings("Super.Trainee", filterRequest)).thenReturn(trainingList);

        mockMvc.perform(get("/api/v1/trainees/{username}/trainings", "Super.Trainee")
                        .param("periodFrom", "2023-12-01T00:00:00")
                        .param("periodTo", "2023-12-31T23:59:00")
                        .param("trainerName", "Trainer Smith")
                        .param("trainingType", "CARDIO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(traineeService, times(1)).getTraineeTrainings(eq("Super.Trainee"), any());
    }
}
