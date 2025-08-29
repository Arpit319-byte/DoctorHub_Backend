package com.example.DoctorHub.mapper;

import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import com.example.DoctorHub.dto.UserRequestDTO;
import com.example.DoctorHub.dto.UserResponseDTO;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.model.User;
import com.example.DoctorHub.Enum.Role;

public class Mapper {

    public static DoctorResponseDTO toDoctorResponseDTO(Doctor doctor){
        DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO();
        doctorResponseDTO.setId(doctor.getId());
        doctorResponseDTO.setName(doctor.getUser().getName());
        doctorResponseDTO.setEmail(doctor.getUser().getEmail());
        doctorResponseDTO.setSpecialty(doctor.getSpecialization());
        doctorResponseDTO.setLicenseNumber(doctor.getLicenseNumber());
        doctorResponseDTO.setRating(doctor.getRating() != null ? doctor.getRating() : null);
        doctorResponseDTO.setCreatedAt(doctor.getCreatedAt());
        doctorResponseDTO.setUpdatedAt(doctor.getUpdatedAt());
        return doctorResponseDTO;
    }


    public static Doctor toDoctor(DoctorRequestDTO doctorRequestDTO){
        // Create User object first
        User user = new User();
        user.setName(doctorRequestDTO.getName());
        user.setEmail(doctorRequestDTO.getEmail());
        user.setPassword(doctorRequestDTO.getPassword());
        user.setRole(Role.DOCTOR);
        
        // Create Doctor object and set the user relationship
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(doctorRequestDTO.getSpecialty());
        doctor.setLicenseNumber(doctorRequestDTO.getLicenseNumber());
        return doctor;
    }
    
    public static UserResponseDTO toUserResponseDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        return userResponseDTO;
    }


    public static User toUser(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());
        return user;
    }
    

}
