package com.epam.gym.main.controller;

import com.epam.gym.main.controller.openapi.TrainerApi;
import com.epam.gym.main.dto.TrainerRequest;
import com.epam.gym.main.dto.TrainerResponse;
import com.epam.gym.main.dto.TrainerTrainingFilterRequest;
import com.epam.gym.main.dto.TrainerUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.service.TrainerService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
@RequestMapping("api/v1/trainers")
@RequiredArgsConstructor
@Slf4j
public class TrainerController implements TrainerApi {

    private final TrainerService trainerService;

    @PostMapping
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    public UserCredentials createTrainer(@Valid @RequestBody TrainerRequest request) {
        log.info("Received request to create trainer");
        return trainerService.create(request);
    }

    @GetMapping("/{username}")
    public TrainerResponse getTrainerByUsername(@PathVariable("username") String username) {
        log.info("Received request to get trainer");
        return trainerService.getTrainerWithTrainees(username);
    }

    @PutMapping("/{username}")
    public TrainerResponse updateTrainer(
            @PathVariable("username") String username,
            @Valid @RequestBody TrainerUpdateRequest request
    ) {
        log.info("Received request to update trainer");
        return trainerService.updateTrainerAndUser(request, username);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainer(@PathVariable("username") String username) {
        log.info("Received request to delete trainer");
        trainerService.delete(username);
    }

    @GetMapping("/{username}/trainings")
    public List<TrainingResponse> getTrainerTrainings(
            @PathVariable(name = "username") String username,
            @Valid @RequestParam(required = false, name = "periodFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodFrom,
            @Valid @RequestParam(required = false, name = "periodTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodTo) {
        log.info("Received request to get trainer trainings");
        var filterRequest = TrainerTrainingFilterRequest.builder()
                .periodFrom(periodFrom)
                .periodTo(periodTo)
                .build();
        return trainerService.findTrainerTrainings(username, filterRequest);
    }
}
