package com.example.DoctorHub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DoctorHub.Enum.Role;
import com.example.DoctorHub.dto.UserRequestDTO;
import com.example.DoctorHub.dto.UserResponseDTO;
import com.example.DoctorHub.exception.UserNotFoundException;
import com.example.DoctorHub.model.User;
import com.example.DoctorHub.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private User user;
    private UserResponseDTO userResponseDTO;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("John Doe");
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setRole(Role.PATIENT);

        user = new User();
        user.setId("user123");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.PATIENT);

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId("user123");
        userResponseDTO.setName("John Doe");
        userResponseDTO.setEmail("john.doe@example.com");
        userResponseDTO.setRole(Role.PATIENT);

        userList = Arrays.asList(user);
    }

    @Test
    void createUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertNotNull(result);
        assertEquals("user123", result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(Role.PATIENT, result.getRole());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserById("user123");

        assertNotNull(result);
        assertEquals("user123", result.getId());
        assertEquals("John Doe", result.getName());

        verify(userRepository).findById("user123");
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById("nonexistent");
        });

        verify(userRepository).findById("nonexistent");
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.updateUser("user123", userRequestDTO);

        assertNotNull(result);
        assertEquals("user123", result.getId());

        verify(userRepository).findById("user123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_NotFound() {
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser("nonexistent", userRequestDTO);
        });

        verify(userRepository).findById("nonexistent");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById("user123");

        boolean result = userService.deleteUser("user123");

        assertTrue(result);
        verify(userRepository).findById("user123");
        verify(userRepository).deleteById("user123");
    }

    @Test
    void deleteUser_NotFound() {
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser("nonexistent");
        });

        verify(userRepository).findById("nonexistent");
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(userList);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user123", result.get(0).getId());

        verify(userRepository).findAll();
    }

    @Test
    void getUserByEmail_Success() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());

        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void getUserByEmail_NotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail("nonexistent@example.com");
        });

        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void verifyUserCredentials_Success() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        boolean result = userService.verifyUserCredentials("john.doe@example.com", "password123");

        assertTrue(result);
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void verifyUserCredentials_WrongPassword() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        boolean result = userService.verifyUserCredentials("john.doe@example.com", "wrongpassword");

        assertFalse(result);
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void verifyUserCredentials_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.verifyUserCredentials("nonexistent@example.com", "password123");
        });

        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void changePassword_Success() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = userService.changePassword("user123", "password123", "newpassword123");

        assertTrue(result);
        verify(userRepository).findById("user123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_WrongOldPassword() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        boolean result = userService.changePassword("user123", "wrongpassword", "newpassword123");

        assertFalse(result);
        verify(userRepository).findById("user123");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_UserNotFound() {
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.changePassword("nonexistent", "oldpassword", "newpassword");
        });

        verify(userRepository).findById("nonexistent");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void isEmailExists_True() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        boolean result = userService.isEmailExists("john.doe@example.com");

        assertTrue(result);
        verify(userRepository).existsByEmail("john.doe@example.com");
    }

    @Test
    void isEmailExists_False() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        boolean result = userService.isEmailExists("nonexistent@example.com");

        assertFalse(result);
        verify(userRepository).existsByEmail("nonexistent@example.com");
    }

    @Test
    void getUsersByRole_Success() {
        when(userRepository.findByRoleString("PATIENT")).thenReturn(userList);

        List<UserResponseDTO> result = userService.getUsersByRole("PATIENT");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Role.PATIENT, result.get(0).getRole());

        verify(userRepository).findByRoleString("PATIENT");
    }

    @Test
    void activateUser_NotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userService.activateUser("user123");
        });
    }

    @Test
    void deactivateUser_NotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userService.deactivateUser("user123");
        });
    }

    @Test
    void resetPassword_NotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userService.resetPassword("john.doe@example.com");
        });
    }
}
