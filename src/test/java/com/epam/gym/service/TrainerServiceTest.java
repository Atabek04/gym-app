package com.epam.gym.service;

import com.epam.gym.model.Trainer;
import com.epam.gym.repository.TrainerRepository;
import com.epam.gym.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainer() {
        Trainer trainer = new Trainer();
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer createdTrainer = trainerService.create(trainer);

        assertThat(createdTrainer).isNotNull();
        assertThat(createdTrainer).isEqualTo(trainer);
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testFindTrainerByIdSuccess() {
        Long trainerId = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.findById(trainerId);

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainerId, foundTrainer.get().getId());
        verify(trainerRepository, times(1)).findById(trainerId);
    }

    @Test
    void testFindTrainerByIdNotFound() {
        Long trainerId = 1L;
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());

        Optional<Trainer> foundTrainer = trainerService.findById(trainerId);

        assertTrue(foundTrainer.isEmpty());
        verify(trainerRepository, times(1)).findById(trainerId);
    }

    @Test
    void testFindAllTrainers() {
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());
        when(trainerRepository.findAll()).thenReturn(trainers);

        List<Trainer> foundTrainers = trainerService.findAll();

        assertEquals(2, foundTrainers.size());
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void testFindAllTrainersEmpty() {
        when(trainerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Trainer> foundTrainers = trainerService.findAll();

        assertTrue(foundTrainers.isEmpty());
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void testFindAllFreeTrainers() {
        String username = "someUsername";
        List<Trainer> freeTrainers = List.of(new Trainer(), new Trainer());
        when(trainerRepository.findAllFreeTrainers(username)).thenReturn(freeTrainers);

        List<Trainer> foundFreeTrainers = trainerService.findAllFreeTrainers(username);

        assertEquals(2, foundFreeTrainers.size());
        verify(trainerRepository, times(1)).findAllFreeTrainers(username);
    }

    @Test
    void testDeleteTrainer() {
        Long trainerId = 1L;

        trainerService.delete(trainerId);

        verify(trainerRepository, times(1)).deleteById(trainerId);
    }
}
