package com.example.DoctorHub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import com.example.DoctorHub.exception.DoctorNotFoundException;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.model.User;
import com.example.DoctorHub.repository.DoctorRepository;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private DoctorRequestDTO doctorRequestDTO;
    private Doctor doctor;
    private User user;
    private DoctorResponseDTO doctorResponseDTO;
    private List<Doctor> doctorList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user123");
        user.setName("Dr. Smith");
        user.setEmail("dr.smith@example.com");
        user.setPassword("password123");

        doctor = new Doctor();
        doctor.setId("doc123");
        doctor.setUser(user);
        doctor.setSpecialization("Cardiology");
        doctor.setLicenseNumber("LIC123456");
        doctor.setRating(4.5);
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedAt(LocalDateTime.now());

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

        doctorList = Arrays.asList(doctor);
    }

    @Test
    void createDoctor_Success() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorResponseDTO result = doctorService.createDoctor(doctorRequestDTO);

        assertNotNull(result);
        assertEquals("doc123", result.getId());
        assertEquals("Dr. Smith", result.getName());
        assertEquals("Cardiology", result.getSpecialty());

        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void getDoctorById_Success() {
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));

        DoctorResponseDTO result = doctorService.getDoctorById("doc123");

        assertNotNull(result);
        assertEquals("doc123", result.getId());
        assertEquals("Dr. Smith", result.getName());

        verify(doctorRepository).findById("doc123");
    }

    @Test
    void getDoctorById_NotFound() {
        when(doctorRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.getDoctorById("nonexistent");
        });

        verify(doctorRepository).findById("nonexistent");
    }

    @Test
    void updateDoctor_Success() {
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorResponseDTO result = doctorService.updateDoctor("doc123", doctorRequestDTO);

        assertNotNull(result);
        assertEquals("doc123", result.getId());

        verify(doctorRepository).findById("doc123");
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void updateDoctor_NotFound() {
        when(doctorRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.updateDoctor("nonexistent", doctorRequestDTO);
        });

        verify(doctorRepository).findById("nonexistent");
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void deleteDoctor_Success() {
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).deleteById("doc123");

        boolean result = doctorService.deleteDoctor("doc123");

        assertTrue(result);
        verify(doctorRepository).findById("doc123");
        verify(doctorRepository).deleteById("doc123");
    }

    @Test
    void deleteDoctor_NotFound() {
        when(doctorRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.deleteDoctor("nonexistent");
        });

        verify(doctorRepository).findById("nonexistent");
        verify(doctorRepository, never()).deleteById(anyString());
    }

    @Test
    void getAllDoctors_Success() {
        when(doctorRepository.findAll()).thenReturn(doctorList);

        List<DoctorResponseDTO> result = doctorService.getAllDoctors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("doc123", result.get(0).getId());

        verify(doctorRepository).findAll();
    }

    @Test
    void getDoctorsBySpecialty_Success() {
        when(doctorRepository.findBySpecialization("Cardiology")).thenReturn(doctorList);

        List<DoctorResponseDTO> result = doctorService.getDoctorsBySpecialty("Cardiology");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialty());

        verify(doctorRepository).findBySpecialization("Cardiology");
    }

    @Test
    void getAvailableDoctors_NotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> {
            doctorService.getAvailableDoctors("2024-01-15", "09:00");
        });
    }

    @Test
    void verifyDoctorLicense_Success() {
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));

        boolean result = doctorService.verifyDoctorLicense("doc123", "LIC123456");

        assertTrue(result);
        verify(doctorRepository).findById("doc123");
    }

    @Test
    void verifyDoctorLicense_WrongLicense() {
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));

        boolean result = doctorService.verifyDoctorLicense("doc123", "WRONGLICENSE");

        assertFalse(result);
        verify(doctorRepository).findById("doc123");
    }

    @Test
    void verifyDoctorLicense_DoctorNotFound() {
        when(doctorRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.verifyDoctorLicense("nonexistent", "LIC123456");
        });

        verify(doctorRepository).findById("nonexistent");
    }

    @Test
    void updateDoctorAvailability_NotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> {
            doctorService.updateDoctorAvailability("doc123", "schedule");
        });
    }

    @Test
    void getDoctorRating_Success() {
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));

        double result = doctorService.getDoctorRating("doc123");

        assertEquals(4.5, result);
        verify(doctorRepository).findById("doc123");
    }

    @Test
    void getDoctorRating_DoctorNotFound() {
        when(doctorRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.getDoctorRating("nonexistent");
        });

        verify(doctorRepository).findById("nonexistent");
    }

    @Test
    void getTopRatedDoctors_Success() {
        when(doctorRepository.findAllOrderByRatingDescAndLimit(5)).thenReturn(doctorList);

        List<DoctorResponseDTO> result = doctorService.getTopRatedDoctors(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("doc123", result.get(0).getId());

        verify(doctorRepository).findAllOrderByRatingDescAndLimit(5);
    }

    @Test
    void getTopRatedDoctors_DefaultLimit() {
        when(doctorRepository.findAllOrderByRatingDescAndLimit(10)).thenReturn(doctorList);

        List<DoctorResponseDTO> result = doctorService.getTopRatedDoctors(10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(doctorRepository).findAllOrderByRatingDescAndLimit(10);
    }
}
