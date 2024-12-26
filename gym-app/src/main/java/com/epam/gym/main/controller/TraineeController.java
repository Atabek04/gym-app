package com.epam.gym.main.controller;

import com.epam.gym.main.controller.openapi.TraineeApi;
import com.epam.gym.main.dto.BasicTrainerResponse;
import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.TraineeResponse;
import com.epam.gym.main.dto.TraineeTrainingFilterRequest;
import com.epam.gym.main.dto.TraineeUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.model.TrainingType;
import com.epam.gym.main.service.TraineeService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/trainees")
@RequiredArgsConstructor
@Slf4j
public class TraineeController implements TraineeApi {

    private final TraineeService traineeService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    public UserCredentials createTrainee(@Valid @RequestBody TraineeRequest request) {
        log.info("Received request to create trainee");
        return traineeService.create(request);
    }

    @Override
    @GetMapping("/{username}")
    public TraineeResponse getTraineeByUsername(@NotBlank @PathVariable("username") String username) {
        log.info("Received request to get trainee by username");
        return traineeService.getTraineeWithTrainers(username);
    }

    @Override
    @PutMapping("/{username}")
    public TraineeResponse updateTrainee(@NotBlank @PathVariable("username") String traineeUsername,
                                         @Valid @RequestBody TraineeUpdateRequest request) {
        log.info("Received request to update trainee");
        return traineeService.updateTraineeAndUser(request, traineeUsername);
    }

    @Override
    @Transactional
    @PutMapping("/{username}/trainers")
    public TraineeResponse updateTrainers(@NotBlank @PathVariable("username") String username,
                                          @RequestBody List<String> trainerUsernames) {
        log.info("Received request to update trainers list for trainee");
        traineeService.updateTrainers(username, trainerUsernames);
        return traineeService.getTraineeWithTrainers(username);
    }

    @Override
    @GetMapping("/{username}/trainers")
    public List<BasicTrainerResponse> getNotAssignedTrainers(@PathVariable("username") String username) {
        return traineeService.getNotAssignedTrainers(username);
    }

    @Override
    @GetMapping("/{username}/trainings")
    public List<TrainingResponse> getTraineeTrainings(
            @NotBlank @PathVariable("username") String username,
            @RequestParam(required = false, name = "periodFrom")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodFrom,
            @RequestParam(required = false, name = "periodTo")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodTo,
            @RequestParam(required = false, name = "trainerName") String trainerName,
            @RequestParam(required = false, name = "trainingType") TrainingType trainingType) {

        log.info("Received request to get trainee trainings");
        var filterRequest = TraineeTrainingFilterRequest.builder()
                .periodFrom(periodFrom)
                .periodTo(periodTo)
                .trainerName(trainerName)
                .trainingType(trainingType)
                .build();
        return traineeService.getTraineeTrainings(username, filterRequest);
    }


    @Override
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainee(@NotBlank @PathVariable("username") String username) {
        log.info("Received request to delete trainee");
        traineeService.delete(username);
    }
}