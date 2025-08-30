package com.example.DoctorHub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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

import com.example.DoctorHub.Enum.AppointmentStatus;
import com.example.DoctorHub.dto.AppointmentRequestDTO;
import com.example.DoctorHub.dto.AppointmentResponseDTO;
import com.example.DoctorHub.exception.AppointmentNotFoundException;
import com.example.DoctorHub.exception.AppointmentSlotNotFoundException;
import com.example.DoctorHub.exception.DoctorNotFoundException;
import com.example.DoctorHub.exception.UserNotFoundException;
import com.example.DoctorHub.model.Appointment;
import com.example.DoctorHub.model.AppointmentSlot;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.model.User;
import com.example.DoctorHub.repository.AppointmentRepository;
import com.example.DoctorHub.repository.AppointmentSlotRepository;
import com.example.DoctorHub.repository.DoctorRepository;
import com.example.DoctorHub.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentSlotRepository appointmentSlotRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentRequestDTO appointmentRequestDTO;
    private Appointment appointment;
    private AppointmentSlot appointmentSlot;
    private Doctor doctor;
    private User patient;
    private User doctorUser;
    private AppointmentResponseDTO appointmentResponseDTO;
    private List<Appointment> appointmentList;

    @BeforeEach
    void setUp() {
        // Setup User objects
        doctorUser = new User();
        doctorUser.setId("user123");
        doctorUser.setName("Dr. Smith");
        doctorUser.setEmail("dr.smith@example.com");

        patient = new User();
        patient.setId("user456");
        patient.setName("John Doe");
        patient.setEmail("john.doe@example.com");

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

        // Setup Appointment
        appointment = new Appointment();
        appointment.setId("apt123");
        appointment.setAppointmentSlot(appointmentSlot);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);

        // Setup DTOs
        appointmentRequestDTO = new AppointmentRequestDTO();
        appointmentRequestDTO.setAppointmentSlotId("slot123");
        appointmentRequestDTO.setDoctorId("doc123");
        appointmentRequestDTO.setPatientId("user456");
        appointmentRequestDTO.setStatus(AppointmentStatus.PENDING);

        appointmentResponseDTO = new AppointmentResponseDTO();
        appointmentResponseDTO.setAppointmentSlotId("slot123");
        appointmentResponseDTO.setDoctorId("doc123");
        appointmentResponseDTO.setPatientId("user456");
        appointmentResponseDTO.setAppointmentStatus(AppointmentStatus.PENDING);

        appointmentList = Arrays.asList(appointment);
    }

    @Test
    void createAppointment_Success() {
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));
        when(userRepository.findById("user456")).thenReturn(Optional.of(patient));
        when(appointmentSlotRepository.save(any(AppointmentSlot.class))).thenReturn(appointmentSlot);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentResponseDTO result = appointmentService.createAppointment(appointmentRequestDTO);

        assertNotNull(result);
        assertEquals("slot123", result.getAppointmentSlotId());
        assertEquals("doc123", result.getDoctorId());
        assertEquals("user456", result.getPatientId());

        verify(appointmentSlotRepository).findById("slot123");
        verify(doctorRepository).findById("doc123");
        verify(userRepository).findById("user456");
        verify(appointmentSlotRepository).save(any(AppointmentSlot.class));
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void createAppointment_SlotNotFound() {
        when(appointmentSlotRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(AppointmentSlotNotFoundException.class, () -> {
            appointmentRequestDTO.setAppointmentSlotId("nonexistent");
            appointmentService.createAppointment(appointmentRequestDTO);
        });

        verify(appointmentSlotRepository).findById("nonexistent");
        verify(doctorRepository, never()).findById(anyString());
    }

    @Test
    void createAppointment_SlotNotAvailable() {
        appointmentSlot.setAvailable(false);
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));

        assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(appointmentRequestDTO);
        });

        verify(appointmentSlotRepository).findById("slot123");
        verify(doctorRepository, never()).findById(anyString());
    }

    @Test
    void createAppointment_DoctorNotFound() {
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));
        when(doctorRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            appointmentRequestDTO.setDoctorId("nonexistent");
            appointmentService.createAppointment(appointmentRequestDTO);
        });

        verify(appointmentSlotRepository).findById("slot123");
        verify(doctorRepository).findById("nonexistent");
        verify(userRepository, never()).findById(anyString());
    }

    @Test
    void createAppointment_PatientNotFound() {
        when(appointmentSlotRepository.findById("slot123")).thenReturn(Optional.of(appointmentSlot));
        when(doctorRepository.findById("doc123")).thenReturn(Optional.of(doctor));
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            appointmentRequestDTO.setPatientId("nonexistent");
            appointmentService.createAppointment(appointmentRequestDTO);
        });

        verify(appointmentSlotRepository).findById("slot123");
        verify(doctorRepository).findById("doc123");
        verify(userRepository).findById("nonexistent");
    }

    @Test
    void getAppointmentById_Success() {
        when(appointmentRepository.findById("apt123")).thenReturn(Optional.of(appointment));

        AppointmentResponseDTO result = appointmentService.getAppointmentById("apt123");

        assertNotNull(result);
        assertEquals("slot123", result.getAppointmentSlotId());

        verify(appointmentRepository).findById("apt123");
    }

    @Test
    void getAppointmentById_NotFound() {
        when(appointmentRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.getAppointmentById("nonexistent");
        });

        verify(appointmentRepository).findById("nonexistent");
    }

    @Test
    void updateAppointment_Success() {
        when(appointmentRepository.findById("apt123")).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentResponseDTO result = appointmentService.updateAppointment("apt123", appointmentRequestDTO);

        assertNotNull(result);
        assertEquals("slot123", result.getAppointmentSlotId());

        verify(appointmentRepository).findById("apt123");
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void updateAppointment_NotFound() {
        when(appointmentRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.updateAppointment("nonexistent", appointmentRequestDTO);
        });

        verify(appointmentRepository).findById("nonexistent");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void deleteAppointment_Success() {
        when(appointmentRepository.findById("apt123")).thenReturn(Optional.of(appointment));
        when(appointmentSlotRepository.save(any(AppointmentSlot.class))).thenReturn(appointmentSlot);
        doNothing().when(appointmentRepository).deleteById("apt123");

        boolean result = appointmentService.deleteAppointment("apt123");

        assertTrue(result);
        verify(appointmentRepository).findById("apt123");
        verify(appointmentSlotRepository).save(any(AppointmentSlot.class));
        verify(appointmentRepository).deleteById("apt123");
    }

    @Test
    void deleteAppointment_NotFound() {
        when(appointmentRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.deleteAppointment("nonexistent");
        });

        verify(appointmentRepository).findById("nonexistent");
        verify(appointmentSlotRepository, never()).save(any(AppointmentSlot.class));
        verify(appointmentRepository, never()).deleteById(anyString());
    }

    @Test
    void getAllAppointments_Success() {
        when(appointmentRepository.findAll()).thenReturn(appointmentList);

        List<AppointmentResponseDTO> result = appointmentService.getAllAppointments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("slot123", result.get(0).getAppointmentSlotId());

        verify(appointmentRepository).findAll();
    }

    @Test
    void getAppointmentsByUserId_Success() {
        when(appointmentRepository.findByPatientId("user456")).thenReturn(appointmentList);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByUserId("user456");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user456", result.get(0).getPatientId());

        verify(appointmentRepository).findByPatientId("user456");
    }

    @Test
    void getAppointmentsByDoctorId_Success() {
        when(appointmentRepository.findByDoctorId("doc123")).thenReturn(appointmentList);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByDoctorId("doc123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("doc123", result.get(0).getDoctorId());

        verify(appointmentRepository).findByDoctorId("doc123");
    }

    @Test
    void getAppointmentsByDate_Success() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        when(appointmentRepository.findByDate(date)).thenReturn(appointmentList);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByDate(date);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(appointmentRepository).findByDate(date);
    }

    @Test
    void getAppointmentsByDateRange_Success() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        when(appointmentRepository.findByDateRange(startDate, endDate)).thenReturn(appointmentList);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByDateRange(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(appointmentRepository).findByDateRange(startDate, endDate);
    }

    @Test
    void getAppointmentsByStatus_Success() {
        when(appointmentRepository.findByAppointmentStatus(AppointmentStatus.PENDING)).thenReturn(appointmentList);

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByStatus(AppointmentStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AppointmentStatus.PENDING, result.get(0).getAppointmentStatus());

        verify(appointmentRepository).findByAppointmentStatus(AppointmentStatus.PENDING);
    }
}
