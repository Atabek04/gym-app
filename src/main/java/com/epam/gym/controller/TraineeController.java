package com.epam.gym.controller;

import com.epam.gym.controller.openapi.TraineeApi;
import com.epam.gym.dto.BasicTrainerResponse;
import com.epam.gym.dto.TraineeRequest;
import com.epam.gym.dto.TraineeResponse;
import com.epam.gym.dto.TraineeTrainingFilterRequest;
import com.epam.gym.dto.TraineeUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserStatusRequest;
import com.epam.gym.service.TraineeService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/trainees")
@RequiredArgsConstructor
@Slf4j
@Secured("Trainee")
public class TraineeController implements TraineeApi {

    private final TraineeService traineeService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    public UserCredentials createTrainee(@RequestBody TraineeRequest request) {
        return traineeService.create(request);
    }

    @Override
    @GetMapping("/{username}")
    public TraineeResponse getTraineeByUsername(@PathVariable String username) {
        return traineeService.getTraineeAndTrainers(username);
    }

    @Override
    @PutMapping("/{username}")
    public TraineeResponse updateTrainee(@PathVariable String username, @RequestBody TraineeUpdateRequest request) {
        return traineeService.updateTraineeAndUser(request, username);
    }

    @Override
    @PutMapping("/{username}/trainers")
    public TraineeResponse updateTrainers(@PathVariable String username, @RequestBody List<String> trainerUsernames) {
        traineeService.updateTrainers(username, trainerUsernames);
        return traineeService.getTraineeAndTrainers(username);
    }

    @Override
    @GetMapping("/{username}/trainers")
    public List<BasicTrainerResponse> getNotAssignedTrainers(@PathVariable String username) {
        return traineeService.getNotAssignedTrainers(username);
    }

    @Override
    @GetMapping("/{username}/trainings")
    public List<TrainingResponse> getTraineeTrainings(@PathVariable String username, @RequestBody TraineeTrainingFilterRequest filterRequest) {
        return traineeService.getTraineeTrainings(username, filterRequest);
    }

    @Override
    @PatchMapping("/{username}/status")
    public void updateTraineeStatus(@PathVariable String username, @RequestBody UserStatusRequest traineeStatusRequest) {
        traineeService.updateTraineeStatus(username, traineeStatusRequest.isActive());
    }

    @Override
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainee(@PathVariable String username) {
        traineeService.delete(username);
    }
}