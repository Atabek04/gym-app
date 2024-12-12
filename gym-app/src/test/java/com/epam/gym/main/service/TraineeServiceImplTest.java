package com.epam.gym.main.service;

import com.epam.gym.main.dto.TraineeRequest;
import com.epam.gym.main.dto.UserCredentials;
import com.epam.gym.main.model.Trainee;
import com.epam.gym.main.model.User;
import com.epam.gym.main.repository.TraineeRepository;
import com.epam.gym.main.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeRepository repository;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_WithValidTraineeRequest_ShouldReturnUserCredentials() {
        var userCredentials = UserCredentials.builder()
                .username("Super.Man")
                .password("t4R1k2L3")
                .build();
        var traineeRequest = TraineeRequest.builder()
                .firstName("Super")
                .lastName("Man")
                .address("Metropolis")
                .dateOfBirth(LocalDate.parse("1978-12-15"))
                .build();

        when(service.create(traineeRequest)).thenReturn(userCredentials);

        var fetchedUserCredentials = service.create(traineeRequest);

        assertNotNull(fetchedUserCredentials);
        assertEquals(userCredentials.username(), fetchedUserCredentials.username());
        assertEquals(userCredentials.password(), fetchedUserCredentials.password());
    }

    @Test
    void createTrainee_WithValidTraineeObject_ShouldReturnUserCredentials() {
        var user = User.builder()
                .username("Super.Man")
                .firstName("Super")
                .lastName("Man")
                .build();
        var trainee = Trainee.builder()
                .id(1L)
                .user(user)
                .address("Metropolis")
                .dateOfBirth(LocalDate.parse("1978-12-15"))
                .trainings(new ArrayList<>())
                .build();

        when(repository.save(trainee)).thenReturn(trainee);

        var fetchedTrainee = service.create(trainee);

        assertTrue(fetchedTrainee.isPresent());
        assertEquals(trainee.getId(), fetchedTrainee.get().getId());
        assertEquals(user.getUsername(), fetchedTrainee.get().getUser().getUsername());
        assertEquals(user.getFirstName(), fetchedTrainee.get().getUser().getFirstName());
        assertEquals(user.getLastName(), fetchedTrainee.get().getUser().getLastName());
        assertEquals(trainee.getAddress(), fetchedTrainee.get().getAddress());
        assertEquals(trainee.getDateOfBirth(), fetchedTrainee.get().getDateOfBirth());
        verify(repository, times(1)).save(trainee);
    }
}