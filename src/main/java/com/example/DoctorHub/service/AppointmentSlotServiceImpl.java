package com.example.DoctorHub.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.DoctorHub.dto.AppointmentSlotRequestDTO;
import com.example.DoctorHub.dto.AppointmentSlotResponseDTO;
import com.example.DoctorHub.exception.AppointmentSlotNotFoundException;
import com.example.DoctorHub.exception.DoctorNotFoundException;
import com.example.DoctorHub.mapper.Mapper;
import com.example.DoctorHub.model.AppointmentSlot;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.repository.AppointmentSlotRepository;
import com.example.DoctorHub.repository.DoctorRepository;

@Service
public class AppointmentSlotServiceImpl implements IAppointmentSlotService{

     private final Logger logger = LoggerFactory.getLogger(AppointmentSlotServiceImpl.class);
     private final AppointmentSlotRepository appointmentSlotRepository;
     private final DoctorRepository doctorRepository;

     public AppointmentSlotServiceImpl(AppointmentSlotRepository _appointmentSlotRepository, DoctorRepository doctorRepository){
        logger.info("Initializing the AppointmentSlotServiceImpl with repositories");
        this.appointmentSlotRepository = _appointmentSlotRepository;
        this.doctorRepository = doctorRepository;
     }

    @Override
    public AppointmentSlotResponseDTO createAppointmentSlot(AppointmentSlotRequestDTO appointmentSlotRequestDTO) {
        logger.info("Creating appointment slot for doctor ID: {} from {} to {}", 
                   appointmentSlotRequestDTO.getDoctorId(),
                   appointmentSlotRequestDTO.getStartTime(),
                   appointmentSlotRequestDTO.getEndDateTime());
        
        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(appointmentSlotRequestDTO.getDoctorId())
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + appointmentSlotRequestDTO.getDoctorId() + " not found"));
        
        // Validate time constraints
        LocalDateTime startTime = appointmentSlotRequestDTO.getStartTime();
        LocalDateTime endTime = appointmentSlotRequestDTO.getEndDateTime();
        
        if (startTime.isAfter(endTime)) {
            logger.error("Start time {} cannot be after end time {}", startTime, endTime);
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        
        if (startTime.isBefore(LocalDateTime.now())) {
            logger.error("Start time {} cannot be in the past", startTime);
            throw new IllegalArgumentException("Start time cannot be in the past");
        }
        
        // Check for time conflicts with existing slots
        List<AppointmentSlot> conflictingSlots = appointmentSlotRepository.findByDoctorIdAndDateAndTimeRange(
            doctor.getId(), 
            startTime.toLocalDate(), 
            startTime, 
            endTime
        );
        
        if (!conflictingSlots.isEmpty()) {
            logger.warn("Time conflict detected for doctor {} between {} and {}", 
                       doctor.getId(), startTime, endTime);
            throw new IllegalArgumentException("Time slot conflicts with existing appointments");
        }
        
        // Create the appointment slot
        AppointmentSlot slot = new AppointmentSlot();
        slot.setDoctor(doctor);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setAvailable(true);
        
        AppointmentSlot savedSlot = appointmentSlotRepository.save(slot);
        logger.info("Successfully created appointment slot with ID: {}", savedSlot.getId());
        
        return Mapper.toAppointmentSlotResponseDTO(savedSlot);
    }

    @Override
    public AppointmentSlotResponseDTO getAppointmentSlotById(String id) {
        logger.debug("Getting appointment slot by ID: {}", id);
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
            .orElseThrow(() -> new AppointmentSlotNotFoundException("Appointment slot with ID " + id + " not found"));
        
        logger.debug("Successfully fetched appointment slot with ID: {}", id);
        return Mapper.toAppointmentSlotResponseDTO(slot);
    }

    @Override
    public AppointmentSlotResponseDTO updateAppointmentSlot(String id, AppointmentSlotRequestDTO appointmentSlotRequestDTO) {
        logger.info("Updating appointment slot with ID: {}", id);
        
        // Check if slot exists
        AppointmentSlot existingSlot = appointmentSlotRepository.findById(id)
            .orElseThrow(() -> new AppointmentSlotNotFoundException("Appointment slot with ID " + id + " not found"));
        
        // Validate doctor exists if changing
        if (!existingSlot.getDoctor().getId().equals(appointmentSlotRequestDTO.getDoctorId())) {
            Doctor doctor = doctorRepository.findById(appointmentSlotRequestDTO.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + appointmentSlotRequestDTO.getDoctorId() + " not found"));
            existingSlot.setDoctor(doctor);
        }
        
        // Validate time constraints
        LocalDateTime startTime = appointmentSlotRequestDTO.getStartTime();
        LocalDateTime endTime = appointmentSlotRequestDTO.getEndDateTime();
        
        if (startTime.isAfter(endTime)) {
            logger.error("Start time {} cannot be after end time {}", startTime, endTime);
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        
        if (startTime.isBefore(LocalDateTime.now())) {
            logger.error("Start time {} cannot be in the past", startTime);
            throw new IllegalArgumentException("Start time cannot be in the past");
        }
        
        // Update the slot
        existingSlot.setStartTime(startTime);
        existingSlot.setEndTime(endTime);
        
        AppointmentSlot updatedSlot = appointmentSlotRepository.save(existingSlot);
        logger.info("Successfully updated appointment slot with ID: {}", id);
        
        return Mapper.toAppointmentSlotResponseDTO(updatedSlot);
    }

    @Override
    public boolean deleteAppointmentSlot(String id) {
        logger.info("Deleting appointment slot with ID: {}", id);
        
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
            .orElseThrow(() -> new AppointmentSlotNotFoundException("Appointment slot with ID " + id + " not found"));
        
        // Check if slot is currently booked
        if (!slot.isAvailable()) {
            logger.warn("Cannot delete appointment slot {} as it is currently booked", id);
            throw new IllegalStateException("Cannot delete a booked appointment slot");
        }
        
        appointmentSlotRepository.deleteById(id);
        logger.info("Successfully deleted appointment slot with ID: {}", id);
        return true;
    }

    @Override
    public List<AppointmentSlotResponseDTO> getAllAppointmentSlots() {
        logger.debug("Getting all appointment slots");
        List<AppointmentSlotResponseDTO> result = appointmentSlotRepository.findAll()
            .stream()
            .map(Mapper::toAppointmentSlotResponseDTO)
            .collect(Collectors.toList());
        
        logger.debug("Successfully fetched {} appointment slots", result.size());
        return result;
    }

    @Override
    public List<AppointmentSlotResponseDTO> getAppointmentSlotsByDoctorId(String doctorId) {
        logger.debug("Getting appointment slots for doctor ID: {}", doctorId);
        
        // Validate doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found");
        }
        
        List<AppointmentSlotResponseDTO> result = appointmentSlotRepository.findByDoctorId(doctorId)
            .stream()
            .map(Mapper::toAppointmentSlotResponseDTO)
            .collect(Collectors.toList());
        
        logger.debug("Successfully fetched {} appointment slots for doctor ID: {}", result.size(), doctorId);
        return result;
    }

    @Override
    public List<AppointmentSlotResponseDTO> getAppointmentSlotsByDoctorIdAndDate(String doctorId, String date) {
        logger.debug("Getting appointment slots for doctor ID: {} on date: {}", doctorId, date);
        
        // Validate doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found");
        }
        
        // Parse date string
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            logger.error("Invalid date format: {}", date);
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }
        
        List<AppointmentSlotResponseDTO> result = appointmentSlotRepository.findByDoctorIdAndDate(doctorId, localDate)
            .stream()
            .map(Mapper::toAppointmentSlotResponseDTO)
            .collect(Collectors.toList());
        
        logger.debug("Successfully fetched {} appointment slots for doctor ID: {} on date: {}", 
                    result.size(), doctorId, date);
        return result;
    }

    @Override
    public List<AppointmentSlotResponseDTO> getAppointmentSlotsByDoctorIdAndDateAndTime(String doctorId, String date, String time) {
        logger.debug("Getting appointment slots for doctor ID: {} on date: {} at time: {}", doctorId, date, time);
        
        // Validate doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found");
        }
        
        // Parse date and time strings
        LocalDate localDate;
        LocalTime localTime;
        try {
            localDate = LocalDate.parse(date);
            localTime = LocalTime.parse(time);
        } catch (Exception e) {
            logger.error("Invalid date or time format: date={}, time={}", date, time);
            throw new IllegalArgumentException("Invalid date or time format. Expected: date=yyyy-MM-dd, time=HH:mm:ss");
        }
        
        LocalDateTime startTime = LocalDateTime.of(localDate, localTime);
        LocalDateTime endTime = startTime.plusHours(1); // Default 1-hour slot
        
        List<AppointmentSlotResponseDTO> result = appointmentSlotRepository
            .findByDoctorIdAndDateAndTimeRange(doctorId, localDate, startTime, endTime)
            .stream()
            .map(Mapper::toAppointmentSlotResponseDTO)
            .collect(Collectors.toList());
        
        logger.debug("Successfully fetched {} appointment slots for doctor ID: {} on date: {} at time: {}", 
                    result.size(), doctorId, date, time);
        return result;
    }

    @Override
    public List<AppointmentSlotResponseDTO> getAppointmentSlotsByDoctorIdAndDateAndTimeAndIsAvailable(String doctorId, String date, String time, boolean isAvailable) {
        logger.debug("Getting appointment slots for doctor ID: {} on date: {} at time: {} with availability: {}", 
                    doctorId, date, time, isAvailable);
        
        // Validate doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found");
        }
        
        // Parse date and time strings
        LocalDate localDate;
        LocalTime localTime;
        try {
            localDate = LocalDate.parse(date);
            localTime = LocalTime.parse(time);
        } catch (Exception e) {
            logger.error("Invalid date or time format: date={}, time={}", date, time);
            throw new IllegalArgumentException("Invalid date or time format. Expected: date=yyyy-MM-dd, time=HH:mm:ss");
        }
        
        LocalDateTime startTime = LocalDateTime.of(localDate, localTime);
        LocalDateTime endTime = startTime.plusHours(1); // Default 1-hour slot
        
        List<AppointmentSlotResponseDTO> result = appointmentSlotRepository
            .findByDoctorIdAndDateAndTimeRange(doctorId, localDate, startTime, endTime)
            .stream()
            .filter(slot -> slot.isAvailable() == isAvailable)
            .map(Mapper::toAppointmentSlotResponseDTO)
            .collect(Collectors.toList());
        
        logger.debug("Successfully fetched {} appointment slots for doctor ID: {} on date: {} at time: {} with availability: {}", 
                    result.size(), doctorId, date, time, isAvailable);
        return result;
    }
    
}
