package com.epam.gym.main.service.impl;

import com.epam.gym.main.dto.TrainerRequest;
import com.epam.gym.main.dto.TrainerResponse;
import com.epam.gym.main.dto.TrainerTrainingFilterRequest;
import com.epam.gym.main.dto.TrainerUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.exception.ResourceNotFoundException;
import com.epam.gym.main.mapper.TrainingMapper;
import com.epam.gym.main.messaging.AuthServiceNotifier;
import com.epam.gym.main.messaging.TrainingReportNotifier;
import com.epam.gym.main.model.Trainer;
import com.epam.gym.main.model.Training;
import com.epam.gym.main.model.UserRole;
import com.epam.gym.main.repository.TrainerRepository;
import com.epam.gym.main.repository.TrainingRepository;
import com.epam.gym.main.service.TrainerService;
import com.epam.gym.main.service.UserService;
import com.epam.gym.main.util.UserType;
import com.epam.gym.main.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epam.gym.main.mapper.TrainerMapper.toTrainer;
import static com.epam.gym.main.mapper.TrainerMapper.toTrainerResponse;
import static com.epam.gym.main.mapper.UserMapper.toUser;
import static com.epam.gym.main.util.UpdateEntityFields.updateTrainerFields;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final TrainingRepository trainingRepository;
    private final TrainingReportNotifier trainingReportNotifier;
    private final AuthServiceNotifier authServiceNotifier;

    @Override
    public Trainer create(Trainer trainer) {
        log.info("Creating trainer with ID: {}", trainer.getId());
        return trainerRepository.save(trainer);
    }

    @Override
    public UserCredentials create(TrainerRequest request) {
        log.debug("Creating trainer for request with name: {}", request.firstName());
        var extractedUser = toUser(request);
        var plainPassword = UserUtils.generateRandomPassword();
        var user = userService.create(extractedUser, UserRole.ROLE_TRAINER, plainPassword)
                .orElseThrow(() -> new ResourceNotFoundException("Failed to create user for trainer request: " +
                        request.firstName() + " " + request.lastName()));

        trainerRepository.save(toTrainer(request, user));
        log.info("Trainer created successfully for username");

        return UserCredentials.builder()
                .username(user.getUsername())
                .password(plainPassword)
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
        log.debug("Updating trainer and user for username: {}", username);

        var trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));

        var updatedUser = userService.update(toUser(request), trainer.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        updateTrainerFields(trainer, toTrainer(request, updatedUser));

        trainerRepository.save(trainer);
        var trainees = trainerRepository.getAssignedTrainees(username);
        log.info("Trainer and user updated successfully");

        return toTrainerResponse(trainer, trainees);
    }

    @Override
    public TrainerResponse getTrainerWithTrainees(String username) {
        log.debug("Fetching trainer and assigned trainees for username: {}", username);
        var trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer with username " + username + " not found"));

        var assignedTrainees = trainerRepository.getAssignedTrainees(username);
        log.info("Successfully fetched trainer with trainees");
        return toTrainerResponse(trainer, assignedTrainees);
    }

    @Override
    public List<TrainingResponse> findTrainerTrainings(String username, TrainerTrainingFilterRequest filterRequest) {
        log.debug("Fetching trainings for trainer: {} using filters: {}", username, filterRequest);

        var trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));

        List<Training> trainings;
        if (filterRequest.getPeriodFrom() == null || filterRequest.getPeriodTo() == null) {
            trainings = trainingRepository.findTrainingsByTrainerUsername(trainer.getUser().getUsername());
        } else {
            trainings = trainingRepository.findTrainingsByTrainerUsernameAndPeriod(
                    trainer.getUser().getUsername(),
                    filterRequest.getPeriodFrom(),
                    filterRequest.getPeriodTo());
        }

        log.info("Successfully fetched trainings for trainer");
        return trainings.stream()
                .map(TrainingMapper::toTrainingResponse)
                .toList();
    }

    @Override
    public void delete(String username) {
        log.debug("Deleting trainer by username: {}", username);
        var trainerId = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"))
                .getId();
        trainerRepository.deleteById(trainerId);

        log.info("Sending request to delete security user");
        authServiceNotifier.deleteUser(username);

        log.info("Trainer deleted successfully");

        log.info("Sending request to remove trainer workload");
        trainingReportNotifier.removeTrainerWorkload(username, UserType.TRAINER);
    }
}
