package com.epam.gym.service;

import com.epam.gym.model.Trainee;
import com.epam.gym.repository.TraineeRepository;
import com.epam.gym.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        Trainee trainee = new Trainee();
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        Optional<Trainee> createdTrainee = traineeService.create(trainee);

        assertTrue(createdTrainee.isPresent());
        assertEquals(trainee, createdTrainee.get());
        verify(traineeRepository, times(1)).save(trainee);
    }


    @Test
    void testFindTraineeByIdSuccess() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.findById(traineeId);

        assertTrue(foundTrainee.isPresent());
        assertEquals(traineeId, foundTrainee.get().getId());
        verify(traineeRepository, times(1)).findById(traineeId);
    }

    @Test
    void testFindTraineeByIdNotFound() {
        Long traineeId = 1L;
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());

        Optional<Trainee> foundTrainee = traineeService.findById(traineeId);

        assertTrue(foundTrainee.isEmpty());
        verify(traineeRepository, times(1)).findById(traineeId);
    }

    @Test
    void testFindAllTrainees() {
        List<Trainee> trainees = List.of(new Trainee(), new Trainee());
        when(traineeRepository.findAll()).thenReturn(trainees);

        List<Trainee> foundTrainees = traineeService.findAll();

        assertEquals(2, foundTrainees.size());
        verify(traineeRepository, times(1)).findAll();
    }

    @Test
    void testFindAllTraineesEmpty() {
        when(traineeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Trainee> foundTrainees = traineeService.findAll();

        assertTrue(foundTrainees.isEmpty());
        verify(traineeRepository, times(1)).findAll();
    }

    @Test
    void testDeleteTrainee() {
        Long traineeId = 1L;

        traineeService.delete(traineeId);

        verify(traineeRepository, times(1)).deleteById(traineeId);
    }
}