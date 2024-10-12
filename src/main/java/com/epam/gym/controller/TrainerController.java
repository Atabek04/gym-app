package com.epam.gym.controller;

import com.epam.gym.dto.TrainerRequest;
import com.epam.gym.dto.TrainerResponse;
import com.epam.gym.dto.TrainerTrainingFilterRequest;
import com.epam.gym.dto.TrainerUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.dto.UserStatusRequest;
import com.epam.gym.security.Secured;
import com.epam.gym.security.UserRole;
import com.epam.gym.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping("v1/trainers")
@RequiredArgsConstructor
@Slf4j
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserCredentials createTrainer(@Valid @RequestBody TrainerRequest request) {
        return trainerService.create(request);
    }

    @Secured({UserRole.ROLE_TRAINER})
    @GetMapping("/{username}")
    public TrainerResponse getTrainerByUsername(@PathVariable("username") String username) {
        return trainerService.getTrainerAndTrainees(username);
    }

    @Secured({UserRole.ROLE_TRAINER})
    @PutMapping("/{username}")
    public TrainerResponse updateTrainer(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerUpdateRequest request
    ) {
        return trainerService.updateTrainerAndUser(request, username);
    }

    @Secured({UserRole.ROLE_TRAINER})
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainer(@PathVariable("username") String username) {
        trainerService.delete(username);
    }

    @Secured({UserRole.ROLE_TRAINER})
    @GetMapping("/{username}/trainings")
    public List<TrainingResponse> getTrainerTrainings(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerTrainingFilterRequest filterRequest
    ) {
        return trainerService.findTrainerTrainings(username, filterRequest);
    }

    @Secured({UserRole.ROLE_TRAINER})
    @PatchMapping("/{username}/status")
    public void updateTrainerStatus(
            @PathVariable("username") String username,
            @RequestBody @Valid UserStatusRequest trainerStatusRequest
    ) {
        trainerService.updateTrainerStatus(username, trainerStatusRequest.isActive());
    }
}
