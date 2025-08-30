package com.example.DoctorHub.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import com.example.DoctorHub.dto.UserRequestDTO;
import com.example.DoctorHub.dto.UserResponseDTO;
import com.example.DoctorHub.dto.AppointmentResponseDTO;
import com.example.DoctorHub.dto.AppointmentSlotResponseDTO;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.model.User;
import com.example.DoctorHub.model.Appointment;
import com.example.DoctorHub.model.AppointmentSlot;
import com.example.DoctorHub.Enum.Role;

public class Mapper {

    private static final Logger logger = LoggerFactory.getLogger(Mapper.class);

    public static DoctorResponseDTO toDoctorResponseDTO(Doctor doctor){
        logger.debug("Mapping Doctor entity to DoctorResponseDTO for doctor ID: {}", doctor.getId());
        DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO();
        doctorResponseDTO.setId(doctor.getId());
        doctorResponseDTO.setName(doctor.getUser().getName());
        doctorResponseDTO.setEmail(doctor.getUser().getEmail());
        doctorResponseDTO.setSpecialty(doctor.getSpecialization());
        doctorResponseDTO.setLicenseNumber(doctor.getLicenseNumber());
        doctorResponseDTO.setRating(doctor.getRating() != null ? doctor.getRating() : null);
        doctorResponseDTO.setCreatedAt(doctor.getCreatedAt());
        doctorResponseDTO.setUpdatedAt(doctor.getUpdatedAt());
        logger.debug("Successfully mapped Doctor entity to DoctorResponseDTO for doctor ID: {}", doctor.getId());
        return doctorResponseDTO;
    }


    public static Doctor toDoctor(DoctorRequestDTO doctorRequestDTO){
        logger.debug("Mapping DoctorRequestDTO to Doctor entity for email: {}", doctorRequestDTO.getEmail());
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
        logger.debug("Successfully mapped DoctorRequestDTO to Doctor entity for email: {}", doctorRequestDTO.getEmail());
        return doctor;
    }
    
    public static UserResponseDTO toUserResponseDTO(User user){
        logger.debug("Mapping User entity to UserResponseDTO for user ID: {}", user.getId());
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        logger.debug("Successfully mapped User entity to UserResponseDTO for user ID: {}", user.getId());
        return userResponseDTO;
    }


    public static User toUser(UserRequestDTO userRequestDTO){
        logger.debug("Mapping UserRequestDTO to User entity for email: {}", userRequestDTO.getEmail());
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());
        logger.debug("Successfully mapped UserRequestDTO to User entity for email: {}", userRequestDTO.getEmail());
        return user;
    }
    
    // Appointment Mappers
    public static AppointmentResponseDTO toAppointmentResponseDTO(Appointment appointment) {
        logger.debug("Mapping Appointment entity to AppointmentResponseDTO for appointment ID: {}", appointment.getId());
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setAppointmentSlotId(appointment.getAppointmentSlot().getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setAppointmentStatus(appointment.getAppointmentStatus());
        logger.debug("Successfully mapped Appointment entity to AppointmentResponseDTO for appointment ID: {}", appointment.getId());
        return dto;
    }
    

    // AppointmentSlot Mappers
    public static AppointmentSlotResponseDTO toAppointmentSlotResponseDTO(AppointmentSlot slot) {
        logger.debug("Mapping AppointmentSlot entity to AppointmentSlotResponseDTO for slot ID: {}", slot.getId());
        AppointmentSlotResponseDTO dto = new AppointmentSlotResponseDTO();
        dto.setDoctorId(slot.getDoctor().getId());
        dto.setStartTime(slot.getStartTime());
        dto.setEndDateTime(slot.getEndTime());
        dto.setAvailable(slot.isAvailable());
        logger.debug("Successfully mapped AppointmentSlot entity to AppointmentSlotResponseDTO for slot ID: {}", slot.getId());
        return dto;
    }
    

}
