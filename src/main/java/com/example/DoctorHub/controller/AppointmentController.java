package com.example.DoctorHub.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DoctorHub.Enum.AppointmentStatus;
import com.example.DoctorHub.dto.AppointmentRequestDTO;
import com.example.DoctorHub.dto.AppointmentResponseDTO;
import com.example.DoctorHub.service.IAppointmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    
    private final IAppointmentService appointmentService;
    
    public AppointmentController(IAppointmentService appointmentService) {
        logger.info("Initializing AppointmentController with AppointmentService");
        this.appointmentService = appointmentService;
    }
    
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody @Valid AppointmentRequestDTO appointmentRequestDTO) {
        logger.info("POST /api/v1/appointments - Creating appointment for slot: {}, doctor: {}, patient: {}", 
                   appointmentRequestDTO.getAppointmentSlotId(), 
                   appointmentRequestDTO.getDoctorId(), 
                   appointmentRequestDTO.getPatientId());
        AppointmentResponseDTO result = appointmentService.createAppointment(appointmentRequestDTO);
        logger.info("Successfully created appointment with ID: {}", result.getAppointmentSlotId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable String id) {
        logger.debug("GET /api/v1/appointments/{} - Fetching appointment by ID", id);
        AppointmentResponseDTO result = appointmentService.getAppointmentById(id);
        logger.debug("Successfully fetched appointment with ID: {}", id);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable String id, @RequestBody @Valid AppointmentRequestDTO appointmentRequestDTO) {
        logger.info("PUT /api/v1/appointments/{} - Updating appointment", id);
        AppointmentResponseDTO result = appointmentService.updateAppointment(id, appointmentRequestDTO);
        logger.info("Successfully updated appointment with ID: {}", id);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String id) {
        logger.info("DELETE /api/v1/appointments/{} - Deleting appointment", id);
        appointmentService.deleteAppointment(id);
        logger.info("Successfully deleted appointment with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        logger.debug("GET /api/v1/appointments - Fetching all appointments");
        List<AppointmentResponseDTO> result = appointmentService.getAllAppointments();
        logger.debug("Successfully fetched {} appointments", result.size());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByUserId(@PathVariable String userId) {
        logger.debug("GET /api/v1/appointments/user/{} - Fetching appointments for user", userId);
        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByUserId(userId);
        logger.debug("Successfully fetched {} appointments for user ID: {}", result.size(), userId);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDoctorId(@PathVariable String doctorId) {
        logger.debug("GET /api/v1/appointments/doctor/{} - Fetching appointments for doctor", doctorId);
        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByDoctorId(doctorId);
        logger.debug("Successfully fetched {} appointments for doctor ID: {}", result.size(), doctorId);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.debug("GET /api/v1/appointments/date/{} - Fetching appointments for date", date);
        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByDate(date);
        logger.debug("Successfully fetched {} appointments for date: {}", result.size(), date);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.debug("GET /api/v1/appointments/date-range - Fetching appointments from {} to {}", startDate, endDate);
        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByDateRange(startDate, endDate);
        logger.debug("Successfully fetched {} appointments from {} to {}", result.size(), startDate, endDate);
        return ResponseEntity.ok(result);
    }
    
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByStatus(@PathVariable String status) {
        logger.debug("GET /api/v1/appointments/status/{} - Fetching appointments by status", status);
        // Convert string to enum - you might want to add validation here
        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByStatus(
            AppointmentStatus.valueOf(status.toUpperCase()));
        logger.debug("Successfully fetched {} appointments with status: {}", result.size(), status);
        return ResponseEntity.ok(result);
    }
}
