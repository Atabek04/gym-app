package com.epam.gym.service;

import com.epam.gym.model.Training;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    @Mock
    private TrainingRepository repository;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUpdateTrainingSuccess() {
        Long trainingId = 1L;
        Training existingTraining = new Training();
        existingTraining.setId(trainingId);
        when(repository.findById(trainingId)).thenReturn(Optional.of(existingTraining));

        Training updatedTraining = new Training();
        updatedTraining.setId(trainingId);

        trainingService.update(updatedTraining, trainingId);

        verify(repository, times(1)).save(updatedTraining);
    }

    @Test
    void testFindTrainingByIdSuccess() {
        Long trainingId = 1L;
        Training training = new Training();
        training.setId(trainingId);
        when(repository.findById(trainingId)).thenReturn(Optional.of(training));

        Optional<Training> foundTraining = trainingService.findById(trainingId);

        assertTrue(foundTraining.isPresent());
        assertEquals(trainingId, foundTraining.get().getId());
        verify(repository, times(1)).findById(trainingId);
    }

    @Test
    void testFindTrainingByIdNotFound() {
        Long trainingId = 1L;
        when(repository.findById(trainingId)).thenReturn(Optional.empty());

        Optional<Training> foundTraining = trainingService.findById(trainingId);

        assertTrue(foundTraining.isEmpty());
        verify(repository, times(1)).findById(trainingId);
    }

    @Test
    void testFindAllTrainings() {
        List<Training> trainings = List.of(new Training(), new Training());
        when(repository.findAll()).thenReturn(trainings);

        List<Training> foundTrainings = trainingService.findAll();

        assertEquals(2, foundTrainings.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindAllTrainingsEmpty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Training> foundTrainings = trainingService.findAll();

        assertTrue(foundTrainings.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindTrainingsByCriteria() {
        Long trainerId = 1L;
        Long traineeId = 2L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        Integer typeId = 1;
        String sortBy = "trainingDate";
        boolean ascending = true;

        List<Training> trainings = List.of(new Training(), new Training());
        Sort sort = Sort.by(sortBy).ascending();
        when(repository.findTrainingsByCriteria(trainerId, traineeId, startDate, endDate, typeId, sort))
                .thenReturn(trainings);

        List<Training> foundTrainings = trainingService.findTrainingsByCriteria(trainerId, traineeId, startDate,
                endDate, typeId, sortBy, ascending);

        assertEquals(2, foundTrainings.size());
        verify(repository, times(1)).findTrainingsByCriteria(trainerId, traineeId, startDate,
                endDate, typeId, sort);
    }

    @Test
    void testDeleteTraining() {
        Long trainingId = 1L;

        trainingService.delete(trainingId);

        verify(repository, times(1)).deleteById(trainingId);
    }
}
