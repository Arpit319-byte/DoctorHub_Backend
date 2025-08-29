package com.example.DoctorHub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.DoctorHub.dto.UserRequestDTO;
import com.example.DoctorHub.dto.UserResponseDTO;
import com.example.DoctorHub.mapper.Mapper;
import com.example.DoctorHub.model.User;
import com.example.DoctorHub.repository.UserRepository;
import com.example.DoctorHub.exception.UserNotFoundException;

@Service
public class UserService implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        logger.info("UserService created");
        this.userRepository = userRepository;
    }
    
    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        logger.info("Creating user");
        User user = Mapper.toUser(userRequestDTO);
        return Mapper.toUserResponseDTO(userRepository.save(user));
    }
    
    @Override
    public UserResponseDTO getUserById(String id) {
        logger.info("Getting user by ID");
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        return Mapper.toUserResponseDTO(user);
    }
    
    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO) {
        logger.info("Updating user");
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        logger.info("User found");
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());
        
        return Mapper.toUserResponseDTO(userRepository.save(user));
    }
    
    @Override
    public boolean deleteUser(String id) {

        logger.info("Deleting user");
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        userRepository.deleteById(id);
        return true;
    }
    
    @Override
    public List<UserResponseDTO> getAllUsers() {
        logger.info("Getting all users");
        return userRepository.findAll()
            .stream()
            .map(Mapper::toUserResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public UserResponseDTO getUserByEmail(String email) {
        logger.info("Getting user by email");
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return Mapper.toUserResponseDTO(user);
    }
    
    @Override
    public boolean verifyUserCredentials(String email, String password) {
        logger.info("Verifying user credentials");
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return user.getPassword().equals(password); // This is NOT secure - implement proper hashing
    }
    
    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        logger.info("Changing password");
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        
        if (user.getPassword().equals(oldPassword)) { // This is NOT secure - implement proper hashing
            user.setPassword(newPassword); // This should hash the password
            userRepository.save(user);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isEmailExists(String email) {
        logger.info("Checking if email exists");
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public List<UserResponseDTO> getUsersByRole(String role) {
        logger.info("Getting users by role");
        return userRepository.findByRoleString(role)
            .stream()
            .map(Mapper::toUserResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean activateUser(String userId) {
        // This would need an 'active' field in User model
        throw new UnsupportedOperationException("Method not implemented yet");
    }
    
    @Override
    public boolean deactivateUser(String userId) {
        // This would need an 'active' field in User model
        throw new UnsupportedOperationException("Method not implemented yet");
    }
    
    @Override
    public boolean resetPassword(String email) {
        // This would need email service integration
        throw new UnsupportedOperationException("Method not implemented yet");
    }


}
