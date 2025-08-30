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

import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import com.example.DoctorHub.exception.DoctorNotFoundException;
import com.example.DoctorHub.service.IDoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    private DoctorRequestDTO doctorRequestDTO;
    private DoctorResponseDTO doctorResponseDTO;
    private List<DoctorResponseDTO> doctorList;

    @BeforeEach
    void setUp() {
        doctorRequestDTO = new DoctorRequestDTO();
        doctorRequestDTO.setName("Dr. Smith");
        doctorRequestDTO.setEmail("dr.smith@example.com");
        doctorRequestDTO.setPassword("password123");
        doctorRequestDTO.setSpecialty("Cardiology");
        doctorRequestDTO.setLicenseNumber("LIC123456");
        doctorRequestDTO.setRating(4.5);

        doctorResponseDTO = new DoctorResponseDTO();
        doctorResponseDTO.setId("doc123");
        doctorResponseDTO.setName("Dr. Smith");
        doctorResponseDTO.setEmail("dr.smith@example.com");
        doctorResponseDTO.setSpecialty("Cardiology");
        doctorResponseDTO.setLicenseNumber("LIC123456");
        doctorResponseDTO.setRating(4.5);
        doctorResponseDTO.setCreatedAt(LocalDateTime.now());
        doctorResponseDTO.setUpdatedAt(LocalDateTime.now());

        doctorList = Arrays.asList(doctorResponseDTO);
    }

    @Test
    void createDoctor_Success() throws Exception {
        when(doctorService.createDoctor(any(DoctorRequestDTO.class))).thenReturn(doctorResponseDTO);

        mockMvc.perform(post("/api/v1/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("doc123"))
                .andExpect(jsonPath("$.name").value("Dr. Smith"))
                .andExpect(jsonPath("$.specialty").value("Cardiology"))
                .andExpect(jsonPath("$.rating").value(4.5));
    }

    @Test
    void createDoctor_ValidationError() throws Exception {
        DoctorRequestDTO invalidDoctor = new DoctorRequestDTO();
        invalidDoctor.setName(""); // Invalid: empty name
        invalidDoctor.setEmail("invalid-email"); // Invalid email format
        invalidDoctor.setPassword("123"); // Too short password

        mockMvc.perform(post("/api/v1/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDoctor)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDoctorById_Success() throws Exception {
        when(doctorService.getDoctorById("doc123")).thenReturn(doctorResponseDTO);

        mockMvc.perform(get("/api/v1/doctors/doc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("doc123"))
                .andExpect(jsonPath("$.name").value("Dr. Smith"));
    }

    @Test
    void getDoctorById_NotFound() throws Exception {
        when(doctorService.getDoctorById("nonexistent")).thenThrow(new DoctorNotFoundException("Doctor not found"));

        mockMvc.perform(get("/api/v1/doctors/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDoctor_Success() throws Exception {
        when(doctorService.updateDoctor(anyString(), any(DoctorRequestDTO.class))).thenReturn(doctorResponseDTO);

        mockMvc.perform(put("/api/v1/doctors/doc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("doc123"));
    }

    @Test
    void updateDoctor_NotFound() throws Exception {
        when(doctorService.updateDoctor(anyString(), any(DoctorRequestDTO.class)))
                .thenThrow(new DoctorNotFoundException("Doctor not found"));

        mockMvc.perform(put("/api/v1/doctors/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDoctor_Success() throws Exception {
        when(doctorService.deleteDoctor("doc123")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/doctors/doc123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDoctor_NotFound() throws Exception {
        when(doctorService.deleteDoctor("nonexistent")).thenThrow(new DoctorNotFoundException("Doctor not found"));

        mockMvc.perform(delete("/api/v1/doctors/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllDoctors_Success() throws Exception {
        when(doctorService.getAllDoctors()).thenReturn(doctorList);

        mockMvc.perform(get("/api/v1/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("doc123"))
                .andExpect(jsonPath("$[0].name").value("Dr. Smith"));
    }

    @Test
    void getDoctorsBySpecialty_Success() throws Exception {
        when(doctorService.getDoctorsBySpecialty("Cardiology")).thenReturn(doctorList);

        mockMvc.perform(get("/api/v1/doctors/specialty/Cardiology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specialty").value("Cardiology"));
    }

    @Test
    void getTopRatedDoctors_Success() throws Exception {
        when(doctorService.getTopRatedDoctors(5)).thenReturn(doctorList);

        mockMvc.perform(get("/api/v1/doctors/top-rated")
                .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(4.5));
    }

    @Test
    void getTopRatedDoctors_DefaultLimit() throws Exception {
        when(doctorService.getTopRatedDoctors(10)).thenReturn(doctorList);

        mockMvc.perform(get("/api/v1/doctors/top-rated"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(4.5));
    }
}
