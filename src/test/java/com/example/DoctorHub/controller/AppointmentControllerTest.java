package com.example.DoctorHub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.DoctorHub.Enum.AppointmentStatus;
import com.example.DoctorHub.dto.AppointmentRequestDTO;
import com.example.DoctorHub.dto.AppointmentResponseDTO;
import com.example.DoctorHub.exception.AppointmentNotFoundException;
import com.example.DoctorHub.service.IAppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentRequestDTO appointmentRequestDTO;
    private AppointmentResponseDTO appointmentResponseDTO;
    private List<AppointmentResponseDTO> appointmentList;

    @BeforeEach
    void setUp() {
        appointmentRequestDTO = new AppointmentRequestDTO();
        appointmentRequestDTO.setAppointmentSlotId("slot123");
        appointmentRequestDTO.setDoctorId("doc123");
        appointmentRequestDTO.setPatientId("user123");
        appointmentRequestDTO.setStatus(AppointmentStatus.PENDING);

        appointmentResponseDTO = new AppointmentResponseDTO();
        appointmentResponseDTO.setAppointmentSlotId("slot123");
        appointmentResponseDTO.setDoctorId("doc123");
        appointmentResponseDTO.setPatientId("user123");
        appointmentResponseDTO.setAppointmentStatus(AppointmentStatus.PENDING);

        appointmentList = Arrays.asList(appointmentResponseDTO);
    }

    @Test
    void createAppointment_Success() throws Exception {
        when(appointmentService.createAppointment(any(AppointmentRequestDTO.class))).thenReturn(appointmentResponseDTO);

        mockMvc.perform(post("/api/v1/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.appointmentSlotId").value("slot123"))
                .andExpect(jsonPath("$.doctorId").value("doc123"))
                .andExpect(jsonPath("$.patientId").value("user123"))
                .andExpect(jsonPath("$.appointmentStatus").value("PENDING"));
    }

    @Test
    void createAppointment_ValidationError() throws Exception {
        AppointmentRequestDTO invalidAppointment = new AppointmentRequestDTO();
        // Missing required fields

        mockMvc.perform(post("/api/v1/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAppointment)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAppointmentById_Success() throws Exception {
        when(appointmentService.getAppointmentById("apt123")).thenReturn(appointmentResponseDTO);

        mockMvc.perform(get("/api/v1/appointments/apt123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentSlotId").value("slot123"));
    }

    @Test
    void getAppointmentById_NotFound() throws Exception {
        when(appointmentService.getAppointmentById("nonexistent")).thenThrow(new AppointmentNotFoundException("Appointment not found"));

        mockMvc.perform(get("/api/v1/appointments/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAppointment_Success() throws Exception {
        when(appointmentService.updateAppointment(anyString(), any(AppointmentRequestDTO.class))).thenReturn(appointmentResponseDTO);

        mockMvc.perform(put("/api/v1/appointments/apt123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentSlotId").value("slot123"));
    }

    @Test
    void updateAppointment_NotFound() throws Exception {
        when(appointmentService.updateAppointment(anyString(), any(AppointmentRequestDTO.class)))
                .thenThrow(new AppointmentNotFoundException("Appointment not found"));

        mockMvc.perform(put("/api/v1/appointments/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAppointment_Success() throws Exception {
        when(appointmentService.deleteAppointment("apt123")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/appointments/apt123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAppointment_NotFound() throws Exception {
        when(appointmentService.deleteAppointment("nonexistent")).thenThrow(new AppointmentNotFoundException("Appointment not found"));

        mockMvc.perform(delete("/api/v1/appointments/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllAppointments_Success() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(appointmentList);

        mockMvc.perform(get("/api/v1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentSlotId").value("slot123"));
    }

    @Test
    void getAppointmentsByUserId_Success() throws Exception {
        when(appointmentService.getAppointmentsByUserId("user123")).thenReturn(appointmentList);

        mockMvc.perform(get("/api/v1/appointments/user/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value("user123"));
    }

    @Test
    void getAppointmentsByDoctorId_Success() throws Exception {
        when(appointmentService.getAppointmentsByDoctorId("doc123")).thenReturn(appointmentList);

        mockMvc.perform(get("/api/v1/appointments/doctor/doc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value("doc123"));
    }

    @Test
    void getAppointmentsByDate_Success() throws Exception {
        when(appointmentService.getAppointmentsByDate(any(LocalDate.class))).thenReturn(appointmentList);

        mockMvc.perform(get("/api/v1/appointments/date/2024-01-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentSlotId").value("slot123"));
    }

    @Test
    void getAppointmentsByDateRange_Success() throws Exception {
        when(appointmentService.getAppointmentsByDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(appointmentList);

        mockMvc.perform(get("/api/v1/appointments/date-range")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentSlotId").value("slot123"));
    }

    @Test
    void getAppointmentsByStatus_Success() throws Exception {
        when(appointmentService.getAppointmentsByStatus(AppointmentStatus.PENDING)).thenReturn(appointmentList);

        mockMvc.perform(get("/api/v1/appointments/status/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentStatus").value("PENDING"));
    }

    @Test
    void getAppointmentsByStatus_UpperCase() throws Exception {
        when(appointmentService.getAppointmentsByStatus(AppointmentStatus.CONFIRMED)).thenReturn(appointmentList);

        mockMvc.perform(get("/api/v1/appointments/status/confirmed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentStatus").value("PENDING")); // Response still shows original status
    }
}
