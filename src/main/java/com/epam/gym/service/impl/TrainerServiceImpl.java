package com.epam.gym.service.impl;

import com.epam.gym.dto.TrainerRequest;
import com.epam.gym.dto.TrainerResponse;
import com.epam.gym.dto.TrainerTrainingFilterRequest;
import com.epam.gym.dto.TrainerUpdateRequest;
import com.epam.gym.dto.TrainingResponse;
import com.epam.gym.dto.UserCredentials;
import com.epam.gym.exception.ResourceNotFoundException;
import com.epam.gym.mapper.TrainingMapper;
import com.epam.gym.model.Trainer;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.gym.mapper.TrainerMapper.toTrainer;
import static com.epam.gym.mapper.TrainerMapper.toTrainerResponse;
import static com.epam.gym.mapper.UserMapper.toUser;
import static com.epam.gym.util.UpdateEntityFields.updateTrainerFields;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;

    @Override
    public Trainer create(Trainer trainer) {
        log.info("Creating trainer with ID: {}", trainer.getId());
        return trainerRepository.save(trainer);
    }

    @Override
    public UserCredentials create(TrainerRequest request) {
        log.info("Creating trainer for request: {} {}", request.firstName(), request.lastName());

        var user = userService.create(toUser(request))
                .orElseThrow(() -> new ResourceNotFoundException("Failed to create user for trainer request: " + request.firstName() + " " + request.lastName()));

        trainerRepository.save(toTrainer(request, user));
        log.info("Trainer created successfully for username: {}", user.getUsername());

        return UserCredentials.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    @Override
    public void update(Trainer updatedTrainer, Long id) {
        log.info("Updating trainer with ID: {}", id);

        Trainer existingTrainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with ID: " + id));

        updateTrainerFields(existingTrainer, updatedTrainer);

        trainerRepository.save(existingTrainer);
        log.info("Trainer with ID: {} updated successfully.", id);
    }

    @Override
    public TrainerResponse updateTrainerAndUser(TrainerUpdateRequest request, String username) {
        log.info("Updating trainer and user for username: {}", username);

        var trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));

        var updatedUser = userService.update(toUser(request), trainer.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        updateTrainerFields(trainer, toTrainer(request, updatedUser));

        trainerRepository.save(trainer);
        var trainees = trainerRepository.getAssignedTrainees(username);
        log.info("Trainer and user updated successfully for username: {}", username);

        return toTrainerResponse(trainer, trainees);
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        log.info("Fetching trainer by ID: {}", id);
        return trainerRepository.findById(id);
    }

    @Override
    public List<Trainer> findAll() {
        log.info("Fetching all trainers.");
        return trainerRepository.findAll();
    }

    @Override
    public TrainerResponse getTrainerAndTrainees(String username) {
        log.info("Fetching trainer and assigned trainees for username: {}", username);
        var trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer with username " + username + " not found"));

        var assignedTrainees = trainerRepository.getAssignedTrainees(username);
        log.info("Successfully fetched trainer and trainees for username: {}", username);
        return toTrainerResponse(trainer, assignedTrainees);
    }

    @Override
    public List<TrainingResponse> findTrainerTrainings(String username, TrainerTrainingFilterRequest filterRequest) {
        log.info("Fetching trainings for trainer: {} using filters: {}", username, filterRequest);
        var trainings = trainerRepository.findTrainerTrainingsByFilters(username, filterRequest.getPeriodFrom(),
                filterRequest.getPeriodTo(), filterRequest.getTraineeName());
        log.info("Successfully fetched trainings for trainer: {}", username);
        return trainings.stream()
                .map(TrainingMapper::toTrainingResponse)
                .toList();
    }

    @Override
    public void updateTrainerStatus(String username, Boolean isActive) {
        log.info("Updating trainer status for username: {}", username);
        var trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer with username " + username + " not found"));

        var user = trainer.getUser();
        user.setActive(isActive);
        userService.update(user, user.getId());

        log.info("Trainer {} has been {}.", username, Boolean.TRUE.equals(isActive) ? "activated" : "deactivated");
    }

    @Override
    public Optional<Trainer> findByUsername(String s) {
        return trainerRepository.findByUserUsername(s);
    }

    @Override
    public List<Trainer> findAllFreeTrainers(String username) {
        log.info("Fetching all free trainers.");
        return trainerRepository.findAllFreeTrainers(username);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting trainer with ID: {}", id);
        trainerRepository.deleteById(id);
        log.info("Trainer with ID: {} deleted successfully.", id);
    }

    @Override
    public void delete(String username) {
        log.info("Deleting trainer by username: {}", username);
        var trainerId = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"))
                .getId();
        delete(trainerId);
        log.info("Trainer deleted successfully: {}", username);
    }
}
