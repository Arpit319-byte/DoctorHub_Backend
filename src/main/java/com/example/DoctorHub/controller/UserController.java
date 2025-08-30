package com.example.DoctorHub.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DoctorHub.dto.UserRequestDTO;
import com.example.DoctorHub.dto.UserResponseDTO;
import com.example.DoctorHub.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final IUserService userService;
    
    public UserController(IUserService userService) {
        logger.info("Initializing UserController with UserService");
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        logger.info("POST /api/v1/users - Creating new user with email: {}", userRequestDTO.getEmail());
        UserResponseDTO result = userService.createUser(userRequestDTO);
        logger.info("Successfully created user with ID: {}", result.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        logger.debug("GET /api/v1/users/{} - Fetching user by ID", id);
        UserResponseDTO result = userService.getUserById(id);
        logger.debug("Successfully fetched user with ID: {}", id);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        logger.info("PUT /api/v1/users/{} - Updating user", id);
        UserResponseDTO result = userService.updateUser(id, userRequestDTO);
        logger.info("Successfully updated user with ID: {}", id);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        logger.info("DELETE /api/v1/users/{} - Deleting user", id);
        userService.deleteUser(id);
        logger.info("Successfully deleted user with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.debug("GET /api/v1/users - Fetching all users");
        List<UserResponseDTO> result = userService.getAllUsers();
        logger.debug("Successfully fetched {} users", result.size());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        logger.debug("GET /api/v1/users/email/{} - Fetching user by email", email);
        UserResponseDTO result = userService.getUserByEmail(email);
        logger.debug("Successfully fetched user with email: {}", email);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String role) {
        logger.debug("GET /api/v1/users/role/{} - Fetching users by role", role);
        List<UserResponseDTO> result = userService.getUsersByRole(role);
        logger.debug("Successfully fetched {} users with role: {}", result.size(), role);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Boolean> changePassword(@PathVariable String id, 
                                               @RequestBody String oldPassword, 
                                               @RequestBody String newPassword) {
        logger.info("POST /api/v1/users/{}/change-password - Changing password for user", id);
        boolean result = userService.changePassword(id, oldPassword, newPassword);
        logger.info("Password change result for user ID {}: {}", id, result);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/email-exists/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        logger.debug("GET /api/v1/users/email-exists/{} - Checking if email exists", email);
        boolean result = userService.isEmailExists(email);
        logger.debug("Email existence check for {}: {}", email, result);
        return ResponseEntity.ok(result);
    }
}
