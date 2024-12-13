package com.epam.gym.main.service.impl;

import com.epam.gym.main.dto.BasicTrainerResponse;
import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.TraineeResponse;
import com.epam.gym.main.dto.TraineeTrainingFilterRequest;
import com.epam.gym.main.dto.TraineeUpdateRequest;
import com.epam.gym.main.dto.TrainingResponse;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.exception.ResourceNotFoundException;
import com.epam.gym.main.mapper.TrainerMapper;
import com.epam.gym.main.mapper.TrainingMapper;
import com.epam.gym.main.messaging.AuthServiceNotifier;
import com.epam.gym.main.messaging.TrainingReportNotifier;
import com.epam.gym.main.model.Trainee;
import com.epam.gym.main.model.Trainer;
import com.epam.gym.main.model.Training;
import com.epam.gym.main.model.UserRole;
import com.epam.gym.main.repository.TraineeRepository;
import com.epam.gym.main.repository.TrainerRepository;
import com.epam.gym.main.repository.TrainingRepository;
import com.epam.gym.main.service.TraineeService;
import com.epam.gym.main.service.UserService;
import com.epam.gym.main.util.UserType;
import com.epam.gym.main.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.gym.main.mapper.TraineeMapper.toTrainee;
import static com.epam.gym.main.mapper.TraineeMapper.toTraineeResponse;
import static com.epam.gym.main.mapper.UserMapper.toUser;
import static com.epam.gym.main.util.Constants.DUMMY_TRAINING_DURATION;
import static com.epam.gym.main.util.Constants.DUMMY_TRAINING_NAME;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingReportNotifier trainingReportNotifier;
    private final AuthServiceNotifier authServiceNotifier;

    @Transactional
    @Override
    public UserCredentials create(TraineeRequest request) {
        log.info("Creating trainee");

        var extractedUser = toUser(request);
        var plainPassword = UserUtils.generateRandomPassword();

        var createdUser = userService.create(extractedUser, UserRole.ROLE_TRAINEE, plainPassword)
                .orElseThrow(() -> new IllegalStateException("Failed to create user for trainee"));

        traineeRepository.save(toTrainee(request, createdUser));
        log.info("Trainee created successfully with username: {}", createdUser.getUsername());

        return UserCredentials.builder()
                .username(createdUser.getUsername())
                .password(plainPassword)
                .build();
    }

    @Override
    public Optional<Trainee> create(Trainee trainee) {
        log.info("Creating trainee with ID: {}", trainee.getId());
        return Optional.of(traineeRepository.save(trainee));
    }

    @Override
    public void update(Trainee newTrainee, Long id) {
        Trainee existingTrainee = traineeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found with id: " + id));

        existingTrainee.setDateOfBirth(newTrainee.getDateOfBirth());
        existingTrainee.setAddress(newTrainee.getAddress());

        traineeRepository.save(existingTrainee);
        log.info("Trainee with ID: {} updated successfully.", id);
    }

    @Override
    public TraineeResponse updateTraineeAndUser(TraineeUpdateRequest request, String username) {
        log.info("Updating trainee and user for username: {}", username);

        var oldTrainee = traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee with this username not found"));

        var updatedUser = userService.update(toUser(request), oldTrainee.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with this username not found"));

        update(toTrainee(request, updatedUser), oldTrainee.getId());

        log.info("Fetching assigned trainers for trainee: {}", username);
        var trainerList = getAssignedTrainers(username).stream().toList();

        var updatedTrainee = findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee with this username not found"));

        log.info("Trainee and user updated successfully for username: {}", username);
        return toTraineeResponse(updatedTrainee, trainerList);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        log.info("Fetching trainee by username: {}", username);
        return traineeRepository.findByUserUsername(username);
    }

    @Override
    @Transactional
    public void updateTrainers(String username, List<String> trainerUsernames) {
        log.info("Updating trainers for trainee: {}", username);

        if (trainerUsernames.isEmpty()) {
            removeAllTrainers(username);
            return;
        }

        Trainee trainee = getTraineeByUsername(username);
        Set<String> currentTrainerUsernames = getCurrentTrainerUsernames(username);

        addNewTrainers(trainerUsernames, currentTrainerUsernames, trainee);
        removeTrainersNotInList(trainerUsernames, username);

        log.info("Successfully updated trainers for trainee: {}", username);
    }

    private Set<String> getCurrentTrainerUsernames(String username) {
        List<Training> currentTrainings = trainingRepository.findByTraineeUsername(username);
        return currentTrainings.stream()
                .map(training -> training.getTrainer().getUser().getUsername())
                .collect(Collectors.toSet());
    }

    private void removeAllTrainers(String username) {
        log.info("Removing all trainers for trainee: {}", username);
        List<Training> currentTrainings = trainingRepository.findByTraineeUsername(username);
        trainingRepository.deleteAll(currentTrainings);
    }

    private void addNewTrainers(List<String> trainerUsernames, Set<String> currentTrainerUsernames, Trainee trainee) {
        List<String> notFoundTrainers = new ArrayList<>();

        for (String trainerUsername : trainerUsernames) {
            if (!currentTrainerUsernames.contains(trainerUsername)) {
                Trainer trainer = trainerRepository.findByUserUsername(trainerUsername)
                        .orElseGet(() -> {
                            notFoundTrainers.add(trainerUsername);
                            return null;
                        });

                if (trainer != null) {
                    createNewTraining(trainee, trainer);
                }
            }
        }

        if (!notFoundTrainers.isEmpty()) {
            log.warn("Some trainers not found: {}", notFoundTrainers);
            throw new ResourceNotFoundException("Trainers not found: " + String.join(", ", notFoundTrainers));
        }
    }

    private void createNewTraining(Trainee trainee, Trainer trainer) {
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(DUMMY_TRAINING_NAME);
        training.setTrainingDate(LocalDateTime.now());
        training.setTrainingTypeId(trainer.getTrainingTypeId());
        training.setTrainingDuration(DUMMY_TRAINING_DURATION);

        trainingRepository.save(training);
        log.info("Training created for trainee: {} with trainer: {}", trainee.getUser().getUsername(),
                trainer.getUser().getUsername());
    }

    private void removeTrainersNotInList(List<String> trainerUsernames, String username) {
        List<Training> trainingsToRemove = trainingRepository.findByTraineeUsername(username).stream()
                .filter(training -> !trainerUsernames.contains(training.getTrainer().getUser().getUsername()))
                .toList();

        if (!trainingsToRemove.isEmpty()) {
            log.info("Removing trainers not in the new list for trainee: {}", username);
            trainingRepository.deleteAll(trainingsToRemove);
        }
    }

    @Transactional
    @Override
    public TraineeResponse getTraineeAndTrainers(String username) {
        log.info("Fetching trainee and trainers by username: {}", username);
        var trainerList = getAssignedTrainers(username).stream().toList();
        var trainee = getTraineeByUsername(username);
        log.info("Successfully fetched trainee and trainers for username: {}", username);
        return toTraineeResponse(trainee, trainerList);
    }

    @Override
    public List<Trainer> getAssignedTrainers(String username) {
        return traineeRepository.getAssignedTrainers(username);
    }

    @Override
    public List<BasicTrainerResponse> getNotAssignedTrainers(String username) {
        log.info("Fetching not assigned trainers for trainee: {}", username);
        var trainers = traineeRepository.getNotAssignedTrainers(username).stream()
                .map(TrainerMapper::toBasicTrainerResponse)
                .toList();
        log.info("Successfully fetched not assigned trainers for trainee: {}", username);
        return trainers;
    }

    @Override
    public List<TrainingResponse> getTraineeTrainings(String username, TraineeTrainingFilterRequest filterRequest) {
        log.info("Fetching trainings for trainee: {} with filters: {}", username, filterRequest);

        Trainee trainee = getTraineeByUsername(username);
        List<Training> trainings;

        if (filterRequest.getPeriodFrom() == null && filterRequest.getPeriodTo() == null
                && filterRequest.getTrainerName() == null && filterRequest.getTrainingType() == null) {
            trainings = getTraineeTrainingsByTraineeID(trainee.getId());
        } else if (filterRequest.getTrainerName() == null && filterRequest.getTrainingType() == null) {
            trainings = getTraineeTrainingsByTraineeIdAndTrainingDateBetween(trainee.getId(), filterRequest);
        } else {
            trainings = getTraineeTrainingByAllCriteria(trainee.getId(), filterRequest);
        }
        log.info("Successfully fetched trainings for trainee: {}", username);
        return trainings.stream().map(TrainingMapper::toTrainingResponse).toList();
    }

    private List<Training> getTraineeTrainingsByTraineeID(Long traineeId) {
        return trainingRepository.findByTraineeId(traineeId);
    }

    private List<Training> getTraineeTrainingsByTraineeIdAndTrainingDateBetween(Long traineeId, TraineeTrainingFilterRequest filterRequest) {
        LocalDateTime periodFrom = filterRequest.getPeriodFrom();
        LocalDateTime periodTo = filterRequest.getPeriodTo();
        return trainingRepository.findByTraineeIdAndTrainingDateBetween(
                traineeId, periodFrom, periodTo);
    }

    private List<Training> getTraineeTrainingByAllCriteria(Long traineeId, TraineeTrainingFilterRequest filterRequest) {
        LocalDateTime periodFrom = filterRequest.getPeriodFrom();
        LocalDateTime periodTo = filterRequest.getPeriodTo();
        Integer trainingTypeId = filterRequest.getTrainingType().getId();
        return trainingRepository.findByAllFilters(
                traineeId,
                periodFrom,
                periodTo,
                filterRequest.getTrainerName(),
                trainingTypeId
        );
    }


    private Trainee getTraineeByUsername(String username) {
        return traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee with username " + username + " not found"));
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting trainee with ID: {}", id);
        traineeRepository.deleteById(id);
        log.info("Trainee with ID: {} deleted successfully.", id);
    }

    @Override
    public void delete(String username) {
        log.info("Deleting trainee by username: {}", username);
        traineeRepository.deleteByUserUsername(username);

        log.info("Sending request to delete security user with username {}", username);
        authServiceNotifier.deleteUser(username);
        log.info("Trainee with username: {} deleted successfully.", username);

        trainingReportNotifier.removeTrainerWorkload(username, UserType.TRAINEE);
    }
}
