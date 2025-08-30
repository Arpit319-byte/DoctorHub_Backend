package com.example.DoctorHub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DoctorHub.dto.AppointmentSlotRequestDTO;
import com.example.DoctorHub.dto.AppointmentSlotResponseDTO;
import com.example.DoctorHub.exception.AppointmentSlotNotFoundException;
import com.example.DoctorHub.exception.DoctorNotFoundException;
import com.example.DoctorHub.model.AppointmentSlot;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.model.User;
import com.example.DoctorHub.repository.AppointmentSlotRepository;
import com.example.DoctorHub.repository.DoctorRepository;

@ExtendWith(MockitoExtension.class)
class AppointmentSlotServiceImplTest {

    @Mock
    private AppointmentSlotRepository appointmentSlotRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentSlotServiceImpl appointmentSlotService;

    private AppointmentSlotRequestDTO appointmentSlotRequestDTO;
    private AppointmentSlot appointmentSlot;
    private Doctor doctor;
    private User doctorUser;
    private AppointmentSlotResponseDTO appointmentSlotResponseDTO;
    private List<AppointmentSlot> appointmentSlotList;

    @BeforeEach
    void setUp() {
        // Setup User
        doctorUser = new User();
        doctorUser.setId("user123");
        doctorUser.setName("Dr. Smith");
        doctorUser.setEmail("dr.smith@example.com");

        // Setup Doctor
        doctor = new Doctor();
        doctor.setId("doc123");
        doctor.setUser(doctorUser);
        doctor.setSpecialization("Cardiology");

        // Setup AppointmentSlot
        appointmentSlot = new AppointmentSlot();
        appointmentSlot.setId("slot123");
        appointmentSlot.setDoctor(doctor);
        appointmentSlot.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        appointmentSlot.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        appointmentSlot.setAvailable(true);

        // Setup DTOs
        appointmentSlotRequestDTO = new AppointmentSlotRequestDTO();
        appointmentSlotRequestDTO.setDoctorId("doc123");
        appointmentSlotRequestDTO.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        appointmentSlotRequestDTO.setEndDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));

        appointmentSlotResponseDTO = new AppointmentSlotResponseDTO();
        appointmentSlotResponseDTO.setDoctorId("doc123");
        appointmentSlotResponseDTO.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        appointmentSlotResponseDTO.setEndDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        appointmentSlotResponseDTO.setAvailable(true);

        appointmentSlotList = Arrays.asList(appointmentSlot);
    }

    @Test
    void createAppointmentSlot_Success() {
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));
        when(appointmentSlotRepository.findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentSlotRepository.save(any(AppointmentSlot.class))).thenReturn(appointmentSlot);

        AppointmentSlotResponseDTO result = appointmentSlotService.createAppointmentSlot(appointmentSlotRequestDTO);

        assertNotNull(result);
        assertEquals("doc123", result.getDoctorId());
        assertTrue(result.isAvailable());

        verify(doctorRepository).findById("doc123");
        verify(appointmentSlotRepository).findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(appointmentSlotRepository).save(any(AppointmentSlot.class));
    }

    @Test
    void createAppointmentSlot_DoctorNotFound() {
        when(doctorRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            appointmentSlotRequestDTO.setDoctorId("nonexistent");
            appointmentSlotService.createAppointmentSlot(appointmentSlotRequestDTO);
        });

        verify(doctorRepository).findById("nonexistent");
        verify(appointmentSlotRepository, never()).save(any(AppointmentSlot.class));
    }

    @Test
    void createAppointmentSlot_InvalidTimeRange() {
        appointmentSlotRequestDTO.setStartTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        appointmentSlotRequestDTO.setEndDateTime(LocalDateTime.of(2024, 1, 15, 9, 0));

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentSlotService.createAppointmentSlot(appointmentSlotRequestDTO);
        });

        verify(doctorRepository, never()).findById(anyString());
        verify(appointmentSlotRepository, never()).save(any(AppointmentSlot.class));
    }

    @Test
    void createAppointmentSlot_PastStartTime() {
        appointmentSlotRequestDTO.setStartTime(LocalDateTime.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentSlotService.createAppointmentSlot(appointmentSlotRequestDTO);
        });

        verify(doctorRepository, never()).findById(anyString());
        verify(appointmentSlotRepository, never()).save(any(AppointmentSlot.class));
    }

    @Test
    void createAppointmentSlot_TimeConflict() {
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));
        when(appointmentSlotRepository.findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(appointmentSlotList);

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentSlotService.createAppointmentSlot(appointmentSlotRequestDTO);
        });

        verify(doctorRepository).findById("doc123");
        verify(appointmentSlotRepository).findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(appointmentSlotRepository, never()).save(any(AppointmentSlot.class));
    }

    @Test
    void getAppointmentSlotById_Success() {
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));

        AppointmentSlotResponseDTO result = appointmentSlotService.getAppointmentSlotById("slot123");

        assertNotNull(result);
        assertEquals("doc123", result.getDoctorId());

        verify(appointmentSlotRepository).findById("slot123");
    }

    @Test
    void getAppointmentSlotById_NotFound() {
        when(appointmentSlotRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(AppointmentSlotNotFoundException.class, () -> {
            appointmentSlotService.getAppointmentSlotById("nonexistent");
        });

        verify(appointmentSlotRepository).findById("nonexistent");
    }

    @Test
    void updateAppointmentSlot_Success() {
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));
        when(appointmentSlotRepository.save(any(AppointmentSlot.class))).thenReturn(appointmentSlot);

        AppointmentSlotResponseDTO result = appointmentSlotService.updateAppointmentSlot("slot123", appointmentSlotRequestDTO);

        assertNotNull(result);
        assertEquals("doc123", result.getDoctorId());

        verify(appointmentSlotRepository).findById("slot123");
        verify(appointmentSlotRepository).save(any(AppointmentSlot.class));
    }

    @Test
    void updateAppointmentSlot_NotFound() {
        when(appointmentSlotRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(AppointmentSlotNotFoundException.class, () -> {
            appointmentSlotService.updateAppointmentSlot("nonexistent", appointmentSlotRequestDTO);
        });

        verify(appointmentSlotRepository).findById("nonexistent");
        verify(appointmentSlotRepository, never()).save(any(AppointmentSlot.class));
    }

    @Test
    void updateAppointmentSlot_InvalidTimeRange() {
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));
        appointmentSlotRequestDTO.setStartTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        appointmentSlotRequestDTO.setEndDateTime(LocalDateTime.of(2024, 1, 15, 9, 0));

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentSlotService.updateAppointmentSlot("slot123", appointmentSlotRequestDTO);
        });

        verify(appointmentSlotRepository).findById("slot123");
        verify(appointmentSlotRepository, never()).save(any(AppointmentSlot.class));
    }

    @Test
    void deleteAppointmentSlot_Success() {
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));
        doNothing().when(appointmentSlotRepository).deleteById("slot123");

        boolean result = appointmentSlotService.deleteAppointmentSlot("slot123");

        assertTrue(result);
        verify(appointmentSlotRepository).findById("slot123");
        verify(appointmentSlotRepository).deleteById("slot123");
    }

    @Test
    void deleteAppointmentSlot_NotFound() {
        when(appointmentSlotRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(AppointmentSlotNotFoundException.class, () -> {
            appointmentSlotService.deleteAppointmentSlot("nonexistent");
        });

        verify(appointmentSlotRepository).findById("nonexistent");
        verify(appointmentSlotRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteAppointmentSlot_BookedSlot() {
        appointmentSlot.setAvailable(false);
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));

        assertThrows(IllegalStateException.class, () -> {
            appointmentSlotService.deleteAppointmentSlot("slot123");
        });

        verify(appointmentSlotRepository).findById("slot123");
        verify(appointmentSlotRepository, never()).deleteById(anyString());
    }

    @Test
    void getAllAppointmentSlots_Success() {
        when(appointmentSlotRepository.findAll()).thenReturn(appointmentSlotList);

        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAllAppointmentSlots();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("doc123", result.get(0).getDoctorId());

        verify(appointmentSlotRepository).findAll();
    }

    @Test
    void getAppointmentSlotsByDoctorId_Success() {
        when(doctorRepository.existsById("doc123")).thenReturn(true);
        when(appointmentSlotRepository.findByDoctorId("doc123")).thenReturn(appointmentSlotList);

        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAppointmentSlotsByDoctorId("doc123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("doc123", result.get(0).getDoctorId());

        verify(doctorRepository).existsById("doc123");
        verify(appointmentSlotRepository).findByDoctorId("doc123");
    }

    @Test
    void getAppointmentSlotsByDoctorId_DoctorNotFound() {
        when(doctorRepository.existsById("nonexistent")).thenReturn(false);

        assertThrows(DoctorNotFoundException.class, () -> {
            appointmentSlotService.getAppointmentSlotsByDoctorId("nonexistent");
        });

        verify(doctorRepository).existsById("nonexistent");
        verify(appointmentSlotRepository, never()).findByDoctorId(anyString());
    }

    @Test
    void getAppointmentSlotsByDoctorIdAndDate_Success() {
        when(doctorRepository.existsById("doc123")).thenReturn(true);
        when(appointmentSlotRepository.findByDoctorIdAndDate("doc123", LocalDate.of(2024, 1, 15))).thenReturn(appointmentSlotList);

        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAppointmentSlotsByDoctorIdAndDate("doc123", "2024-01-15");

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(doctorRepository).existsById("doc123");
        verify(appointmentSlotRepository).findByDoctorIdAndDate("doc123", LocalDate.of(2024, 1, 15));
    }

    @Test
    void getAppointmentSlotsByDoctorIdAndDate_InvalidDate() {
        when(doctorRepository.existsById("doc123")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentSlotService.getAppointmentSlotsByDoctorIdAndDate("doc123", "invalid-date");
        });

        verify(doctorRepository).existsById("doc123");
        verify(appointmentSlotRepository, never()).findByDoctorIdAndDate(anyString(), any(LocalDate.class));
    }

    @Test
    void getAppointmentSlotsByDoctorIdAndDateAndTime_Success() {
        when(doctorRepository.existsById("doc123")).thenReturn(true);
        when(appointmentSlotRepository.findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(appointmentSlotList);

        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAppointmentSlotsByDoctorIdAndDateAndTime("doc123", "2024-01-15", "09:00");

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(doctorRepository).existsById("doc123");
        verify(appointmentSlotRepository).findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getAppointmentSlotsByDoctorIdAndDateAndTime_InvalidTime() {
        when(doctorRepository.existsById("doc123")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentSlotService.getAppointmentSlotsByDoctorIdAndDateAndTime("doc123", "2024-01-15", "invalid-time");
        });

        verify(doctorRepository).existsById("doc123");
        verify(appointmentSlotRepository, never()).findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getAppointmentSlotsByDoctorIdAndDateAndTimeAndIsAvailable_Success() {
        when(doctorRepository.existsById("doc123")).thenReturn(true);
        when(appointmentSlotRepository.findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(appointmentSlotList);

        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAppointmentSlotsByDoctorIdAndDateAndTimeAndIsAvailable("doc123", "2024-01-15", "09:00", true);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(doctorRepository).existsById("doc123");
        verify(appointmentSlotRepository).findByDoctorIdAndDateAndTimeRange(anyString(), any(LocalDate.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}
