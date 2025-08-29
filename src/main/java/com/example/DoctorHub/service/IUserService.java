package com.example.DoctorHub.service;

import java.util.List;
import com.example.DoctorHub.dto.UserRequestDTO;
import com.example.DoctorHub.dto.UserResponseDTO;

public interface IUserService {
    
    // CRUD Operations
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO getUserById(String id);
    UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO);
    boolean deleteUser(String id);
    List<UserResponseDTO> getAllUsers();
    
    // Authentication & Business Logic
    UserResponseDTO getUserByEmail(String email);
    boolean verifyUserCredentials(String email, String password);
    boolean changePassword(String userId, String oldPassword, String newPassword);
    boolean isEmailExists(String email);
    List<UserResponseDTO> getUsersByRole(String role);
    
    // User Management
    boolean activateUser(String userId);
    boolean deactivateUser(String userId);
    boolean resetPassword(String email);
}
