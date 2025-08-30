package com.example.DoctorHub.service;

import java.time.LocalDate;
import java.util.List;

import com.example.DoctorHub.Enum.AppointmentStatus;
import com.example.DoctorHub.dto.AppointmentRequestDTO;
import com.example.DoctorHub.dto.AppointmentResponseDTO;

public interface IAppointmentService {

    // CRUD Operations
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO appointmentRequestDTO);
    AppointmentResponseDTO getAppointmentById(String id);
    AppointmentResponseDTO updateAppointment(String id, AppointmentRequestDTO appointmentRequestDTO);
    boolean deleteAppointment(String id);
    List<AppointmentResponseDTO> getAllAppointments();
    
    // Appointment Retrieval by Different Criteria
    List<AppointmentResponseDTO> getAppointmentsByUserId(String userId);
    List<AppointmentResponseDTO> getAppointmentsByDoctorId(String doctorId);
    List<AppointmentResponseDTO> getAppointmentsByDate(LocalDate date);
    List<AppointmentResponseDTO> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate);
    List<AppointmentResponseDTO> getAppointmentsByStatus(AppointmentStatus status);
    
}

