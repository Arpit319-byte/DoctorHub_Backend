package com.example.DoctorHub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.DoctorHub.dto.AppointmentSlotRequestDTO;
import com.example.DoctorHub.dto.AppointmentSlotResponseDTO;
import com.example.DoctorHub.exception.AppointmentSlotNotFoundException;
import com.example.DoctorHub.service.IAppointmentSlotService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AppointmentSlotController.class)
class AppointmentSlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAppointmentSlotService appointmentSlotService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentSlotRequestDTO appointmentSlotRequestDTO;
    private AppointmentSlotResponseDTO appointmentSlotResponseDTO;
    private List<AppointmentSlotResponseDTO> appointmentSlotList;

    @BeforeEach
    void setUp() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 15, 10, 0);

        appointmentSlotRequestDTO = new AppointmentSlotRequestDTO();
        appointmentSlotRequestDTO.setDoctorId("doc123");
        appointmentSlotRequestDTO.setStartTime(startTime);
        appointmentSlotRequestDTO.setEndDateTime(endTime);

        appointmentSlotResponseDTO = new AppointmentSlotResponseDTO();
        appointmentSlotResponseDTO.setDoctorId("doc123");
        appointmentSlotResponseDTO.setStartTime(startTime);
        appointmentSlotResponseDTO.setEndDateTime(endTime);
        appointmentSlotResponseDTO.setAvailable(true);

        appointmentSlotList = Arrays.asList(appointmentSlotResponseDTO);
    }

    @Test
    void createAppointmentSlot_Success() throws Exception {
        when(appointmentSlotService.createAppointmentSlot(any(AppointmentSlotRequestDTO.class))).thenReturn(appointmentSlotResponseDTO);

        mockMvc.perform(post("/api/v1/appointment-slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentSlotRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value("doc123"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void createAppointmentSlot_ValidationError() throws Exception {
        AppointmentSlotRequestDTO invalidSlot = new AppointmentSlotRequestDTO();
        // Missing required fields

        mockMvc.perform(post("/api/v1/appointment-slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidSlot)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAppointmentSlotById_Success() throws Exception {
        when(appointmentSlotService.getAppointmentSlotById("slot123")).thenReturn(appointmentSlotResponseDTO);

        mockMvc.perform(get("/api/v1/appointment-slots/slot123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value("doc123"));
    }

    @Test
    void getAppointmentSlotById_NotFound() throws Exception {
        when(appointmentSlotService.getAppointmentSlotById("nonexistent")).thenThrow(new AppointmentSlotNotFoundException("Appointment slot not found"));

        mockMvc.perform(get("/api/v1/appointment-slots/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAppointmentSlot_Success() throws Exception {
        when(appointmentSlotService.updateAppointmentSlot(anyString(), any(AppointmentSlotRequestDTO.class))).thenReturn(appointmentSlotResponseDTO);

        mockMvc.perform(put("/api/v1/appointment-slots/slot123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentSlotRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value("doc123"));
    }

    @Test
    void updateAppointmentSlot_NotFound() throws Exception {
        when(appointmentSlotService.updateAppointmentSlot(anyString(), any(AppointmentSlotRequestDTO.class)))
                .thenThrow(new AppointmentSlotNotFoundException("Appointment slot not found"));

        mockMvc.perform(put("/api/v1/appointment-slots/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentSlotRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAppointmentSlot_Success() throws Exception {
        when(appointmentSlotService.deleteAppointmentSlot("slot123")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/appointment-slots/slot123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAppointmentSlot_NotFound() throws Exception {
        when(appointmentSlotService.deleteAppointmentSlot("nonexistent")).thenThrow(new AppointmentSlotNotFoundException("Appointment slot not found"));

        mockMvc.perform(delete("/api/v1/appointment-slots/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllAppointmentSlots_Success() throws Exception {
        when(appointmentSlotService.getAllAppointmentSlots()).thenReturn(appointmentSlotList);

        mockMvc.perform(get("/api/v1/appointment-slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value("doc123"));
    }

    @Test
    void getAppointmentSlotsByDoctorId_Success() throws Exception {
        when(appointmentSlotService.getAppointmentSlotsByDoctorId("doc123")).thenReturn(appointmentSlotList);

        mockMvc.perform(get("/api/v1/appointment-slots/doctor/doc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value("doc123"));
    }

    @Test
    void getAppointmentSlotsByDoctorIdAndDate_Success() throws Exception {
        when(appointmentSlotService.getAppointmentSlotsByDoctorIdAndDate("doc123", "2024-01-15")).thenReturn(appointmentSlotList);

        mockMvc.perform(get("/api/v1/appointment-slots/doctor/doc123/date/2024-01-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value("doc123"));
    }

    @Test
    void getAppointmentSlotsByDoctorIdAndDateAndTime_Success() throws Exception {
        when(appointmentSlotService.getAppointmentSlotsByDoctorIdAndDateAndTime("doc123", "2024-01-15", "09:00")).thenReturn(appointmentSlotList);

        mockMvc.perform(get("/api/v1/appointment-slots/doctor/doc123/date/2024-01-15/time/09:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value("doc123"));
    }

    @Test
    void getAppointmentSlotsByDoctorIdAndDateAndTimeAndIsAvailable_Success() throws Exception {
        when(appointmentSlotService.getAppointmentSlotsByDoctorIdAndDateAndTimeAndIsAvailable("doc123", "2024-01-15", "09:00", true)).thenReturn(appointmentSlotList);

        mockMvc.perform(get("/api/v1/appointment-slots/doctor/doc123/date/2024-01-15/time/09:00/available/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value("doc123"));
    }
}
