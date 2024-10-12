package com.epam.gym.service;

import com.epam.gym.exception.ResourceNotFoundException;
import com.epam.gym.model.User;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() {
        User user = User.builder()
                .firstName("Test")
                .lastName("User")
                .username("Test.User")
                .build();
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> createdUser = userService.create(user);

        assertTrue(createdUser.isPresent());
        assertEquals("Test.User", createdUser.get().getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserSuccess() {
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
        User existingUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .password("oldPassword")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> updatedUser = userService.update(user, 1L);

        assertTrue(updatedUser.isPresent());
        assertEquals(1L, updatedUser.get().getId());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        User user = new User();
        user.setId(1L);
        when(userRepository.save(user)).thenReturn(user);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.update(user, 1L),
                "Expected update() to throw, but it didn't"
        );

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUser() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByUsernameAndPassword() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        when(userRepository.findByUsernameAndPassword("testUser", "testPass")).thenReturn(user);

        Optional<User> foundUser = userService.findByUsernameAndPassword("testUser", "testPass");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findByUsernameAndPassword("testUser", "testPass");
    }

    @Test
    void testChangeUserPassword() {
        userService.changePassword("testUser", "newPass");
        verify(userRepository, times(1)).changePassword("testUser", "newPass");
    }

    @Test
    void testActivateUser() {
        userService.activateUser("testUser");
        verify(userRepository, times(1)).activateUser("testUser");
    }

    @Test
    void testDeactivateUser() {
        userService.deactivateUser("testUser");
        verify(userRepository, times(1)).deactivateUser("testUser");
    }

    @Test
    void testFindUserByUsername() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        Optional<User> foundUser = userService.findUserByUsername("testUser");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }
}