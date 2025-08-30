package com.example.DoctorHub.service;

import java.util.List;

import com.example.DoctorHub.dto.AppointmentSlotRequestDTO;
import com.example.DoctorHub.dto.AppointmentSlotResponseDTO;

public interface IAppointmentSlotService {
    
    //CRUD Operations
    AppointmentSlotResponseDTO createAppointmentSlot(AppointmentSlotRequestDTO appointmentSlotRequestDTO);
    AppointmentSlotResponseDTO getAppointmentSlotById(String id);
    AppointmentSlotResponseDTO updateAppointmentSlot(String id, AppointmentSlotRequestDTO appointmentSlotRequestDTO);
    boolean deleteAppointmentSlot(String id);

    //Search Operations
    List<AppointmentSlotResponseDTO> getAllAppointmentSlots();
    List<AppointmentSlotResponseDTO> getAppointmentSlotsByDoctorId(String doctorId);
    List<AppointmentSlotResponseDTO> getAppointmentSlotsByDoctorIdAndDate(String doctorId, String date);
    List<AppointmentSlotResponseDTO> getAppointmentSlotsByDoctorIdAndDateAndTime(String doctorId, String date, String time);
    List<AppointmentSlotResponseDTO> getAppointmentSlotsByDoctorIdAndDateAndTimeAndIsAvailable(String doctorId, String date, String time, boolean isAvailable);

    
}
