package com.example.DoctorHub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.DoctorHub.Enum.Role;
import com.example.DoctorHub.dto.UserRequestDTO;
import com.example.DoctorHub.dto.UserResponseDTO;
import com.example.DoctorHub.exception.UserNotFoundException;
import com.example.DoctorHub.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private List<UserResponseDTO> userList;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("John Doe");
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setRole(Role.PATIENT);

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId("user123");
        userResponseDTO.setName("John Doe");
        userResponseDTO.setEmail("john.doe@example.com");
        userResponseDTO.setRole(Role.PATIENT);

        userList = Arrays.asList(userResponseDTO);
    }

    @Test
    void createUser_Success() throws Exception {
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user123"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.role").value("PATIENT"));
    }

    @Test
    void createUser_ValidationError() throws Exception {
        UserRequestDTO invalidUser = new UserRequestDTO();
        invalidUser.setName(""); // Invalid: empty name
        invalidUser.setEmail("invalid-email"); // Invalid email format

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_Success() throws Exception {
        when(userService.getUserById("user123")).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/v1/users/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user123"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById("nonexistent")).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_Success() throws Exception {
        when(userService.updateUser(anyString(), any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(put("/api/v1/users/user123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user123"));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        when(userService.updateUser(anyString(), any(UserRequestDTO.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/api/v1/users/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_Success() throws Exception {
        when(userService.deleteUser("user123")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/users/user123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        when(userService.deleteUser("nonexistent")).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(delete("/api/v1/users/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_Success() throws Exception {
        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("user123"))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void getUserByEmail_Success() throws Exception {
        when(userService.getUserByEmail("john.doe@example.com")).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/v1/users/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getUserByEmail_NotFound() throws Exception {
        when(userService.getUserByEmail("nonexistent@example.com"))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/email/nonexistent@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersByRole_Success() throws Exception {
        when(userService.getUsersByRole("PATIENT")).thenReturn(userList);

        mockMvc.perform(get("/api/v1/users/role/PATIENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("PATIENT"));
    }

    @Test
    void changePassword_Success() throws Exception {
        when(userService.changePassword(anyString(), anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/api/v1/users/user123/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPassword\":\"old123\",\"newPassword\":\"new123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void checkEmailExists_True() throws Exception {
        when(userService.isEmailExists("john.doe@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/v1/users/email-exists/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void checkEmailExists_False() throws Exception {
        when(userService.isEmailExists("nonexistent@example.com")).thenReturn(false);

        mockMvc.perform(get("/api/v1/users/email-exists/nonexistent@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
