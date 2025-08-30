package com.example.DoctorHub.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DoctorHub.dto.AppointmentSlotRequestDTO;
import com.example.DoctorHub.dto.AppointmentSlotResponseDTO;
import com.example.DoctorHub.service.IAppointmentSlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/appointment-slots")
public class AppointmentSlotController {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentSlotController.class);
    
    private final IAppointmentSlotService appointmentSlotService;
    
    public AppointmentSlotController(IAppointmentSlotService appointmentSlotService) {
        logger.info("Initializing AppointmentSlotController with AppointmentSlotService");
        this.appointmentSlotService = appointmentSlotService;
    }
    
    @PostMapping
    public ResponseEntity<AppointmentSlotResponseDTO> createAppointmentSlot(@RequestBody @Valid AppointmentSlotRequestDTO appointmentSlotRequestDTO) {
        logger.info("POST /api/v1/appointment-slots - Creating appointment slot for doctor: {} from {} to {}", 
                   appointmentSlotRequestDTO.getDoctorId(),
                   appointmentSlotRequestDTO.getStartTime(),
                   appointmentSlotRequestDTO.getEndDateTime());
        AppointmentSlotResponseDTO result = appointmentSlotService.createAppointmentSlot(appointmentSlotRequestDTO);
        logger.info("Successfully created appointment slot");
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentSlotResponseDTO> getAppointmentSlotById(@PathVariable String id) {
        logger.debug("GET /api/v1/appointment-slots/{} - Fetching appointment slot by ID", id);
        AppointmentSlotResponseDTO result = appointmentSlotService.getAppointmentSlotById(id);
        logger.debug("Successfully fetched appointment slot with ID: {}", id);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentSlotResponseDTO> updateAppointmentSlot(@PathVariable String id, @RequestBody @Valid AppointmentSlotRequestDTO appointmentSlotRequestDTO) {
        logger.info("PUT /api/v1/appointment-slots/{} - Updating appointment slot", id);
        AppointmentSlotResponseDTO result = appointmentSlotService.updateAppointmentSlot(id, appointmentSlotRequestDTO);
        logger.info("Successfully updated appointment slot with ID: {}", id);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentSlot(@PathVariable String id) {
        logger.info("DELETE /api/v1/appointment-slots/{} - Deleting appointment slot", id);
        appointmentSlotService.deleteAppointmentSlot(id);
        logger.info("Successfully deleted appointment slot with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<AppointmentSlotResponseDTO>> getAllAppointmentSlots() {
        logger.debug("GET /api/v1/appointment-slots - Fetching all appointment slots");
        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAllAppointmentSlots();
        logger.debug("Successfully fetched {} appointment slots", result.size());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentSlotResponseDTO>> getAppointmentSlotsByDoctorId(@PathVariable String doctorId) {
        logger.debug("GET /api/v1/appointment-slots/doctor/{} - Fetching appointment slots for doctor", doctorId);
        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAppointmentSlotsByDoctorId(doctorId);
        logger.debug("Successfully fetched {} appointment slots for doctor ID: {}", result.size(), doctorId);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<List<AppointmentSlotResponseDTO>> getAppointmentSlotsByDoctorIdAndDate(
            @PathVariable String doctorId, @PathVariable String date) {
        logger.debug("GET /api/v1/appointment-slots/doctor/{}/date/{} - Fetching appointment slots for doctor on date", doctorId, date);
        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAppointmentSlotsByDoctorIdAndDate(doctorId, date);
        logger.debug("Successfully fetched {} appointment slots for doctor ID: {} on date: {}", result.size(), doctorId, date);
        return ResponseEntity.ok(result);
    }

    
    @GetMapping("/doctor/{doctorId}/date/{date}/time/{time}")
    public ResponseEntity<List<AppointmentSlotResponseDTO>> getAppointmentSlotsByDoctorIdAndDateAndTime(
            @PathVariable String doctorId, @PathVariable String date, @PathVariable String time) {
        logger.debug("GET /api/v1/appointment-slots/doctor/{}/date/{}/time/{} - Fetching appointment slots for doctor on date and time", doctorId, date, time);
        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAppointmentSlotsByDoctorIdAndDateAndTime(doctorId, date, time);
        logger.debug("Successfully fetched {} appointment slots for doctor ID: {} on date: {} at time: {}", result.size(), doctorId, date, time);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/doctor/{doctorId}/date/{date}/time/{time}/available/{isAvailable}")
    public ResponseEntity<List<AppointmentSlotResponseDTO>> getAppointmentSlotsByDoctorIdAndDateAndTimeAndIsAvailable(
            @PathVariable String doctorId, @PathVariable String date, @PathVariable String time, @PathVariable boolean isAvailable) {
        logger.debug("GET /api/v1/appointment-slots/doctor/{}/date/{}/time/{}/available/{} - Fetching appointment slots for doctor on date and time with availability", doctorId, date, time, isAvailable);
        List<AppointmentSlotResponseDTO> result = appointmentSlotService.getAppointmentSlotsByDoctorIdAndDateAndTimeAndIsAvailable(doctorId, date, time, isAvailable);
        logger.debug("Successfully fetched {} appointment slots for doctor ID: {} on date: {} at time: {} with availability: {}", result.size(), doctorId, date, time, isAvailable);
        return ResponseEntity.ok(result);
    }
}
