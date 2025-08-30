package com.example.DoctorHub.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.DoctorHub.Enum.AppointmentStatus;
import com.example.DoctorHub.dto.AppointmentRequestDTO;
import com.example.DoctorHub.dto.AppointmentResponseDTO;
import com.example.DoctorHub.mapper.Mapper;
import com.example.DoctorHub.model.Appointment;
import com.example.DoctorHub.model.AppointmentSlot;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.model.User;
import com.example.DoctorHub.repository.AppointmentRepository;
import com.example.DoctorHub.repository.AppointmentSlotRepository;
import com.example.DoctorHub.repository.DoctorRepository;
import com.example.DoctorHub.repository.UserRepository;
import com.example.DoctorHub.exception.AppointmentNotFoundException;
import com.example.DoctorHub.exception.AppointmentSlotNotFoundException;
import com.example.DoctorHub.exception.DoctorNotFoundException;
import com.example.DoctorHub.exception.UserNotFoundException;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);
    
    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                               AppointmentSlotRepository appointmentSlotRepository,
                               DoctorRepository doctorRepository,
                               UserRepository userRepository) {
        logger.info("Initializing AppointmentServiceImpl with repositories");
        this.appointmentRepository = appointmentRepository;
        this.appointmentSlotRepository = appointmentSlotRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        logger.info("Creating appointment for slot ID: {}, doctor ID: {}, patient ID: {}", 
                   appointmentRequestDTO.getAppointmentSlotId(), 
                   appointmentRequestDTO.getDoctorId(), 
                   appointmentRequestDTO.getPatientId());
        
        // Validate appointment slot exists and is available
        AppointmentSlot slot = appointmentSlotRepository.findById(appointmentRequestDTO.getAppointmentSlotId())
            .orElseThrow(() -> new AppointmentSlotNotFoundException("Appointment slot not found"));
        
        if (!slot.isAvailable()) {
            logger.warn("Appointment slot {} is not available", appointmentRequestDTO.getAppointmentSlotId());
            throw new RuntimeException("Appointment slot is not available");
        }
        
        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(appointmentRequestDTO.getDoctorId())
            .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
        
        // Validate patient exists
        User patient = userRepository.findById(appointmentRequestDTO.getPatientId())
            .orElseThrow(() -> new UserNotFoundException("Patient not found"));
        
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setAppointmentSlot(slot);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentStatus(appointmentRequestDTO.getStatus());
        
        // Mark slot as unavailable
        slot.setAvailable(false);
        appointmentSlotRepository.save(slot);
        
        // Save appointment
        Appointment savedAppointment = appointmentRepository.save(appointment);
        logger.info("Successfully created appointment with ID: {}", savedAppointment.getId());
        return Mapper.toAppointmentResponseDTO(savedAppointment);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(String id) {
        logger.debug("Fetching appointment by ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new AppointmentNotFoundException("Appointment with ID " + id + " not found"));
        logger.debug("Successfully fetched appointment with ID: {}", id);
        return Mapper.toAppointmentResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO updateAppointment(String id, AppointmentRequestDTO appointmentRequestDTO) {
        logger.info("Updating appointment with ID: {} to status: {}", id, appointmentRequestDTO.getStatus());
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new AppointmentNotFoundException("Appointment with ID " + id + " not found"));
        
        // Update appointment status
        appointment.setAppointmentStatus(appointmentRequestDTO.getStatus());
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        logger.info("Successfully updated appointment with ID: {}", id);
        return Mapper.toAppointmentResponseDTO(savedAppointment);
    }

    @Override
    public boolean deleteAppointment(String id) {
        logger.info("Deleting appointment with ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new AppointmentNotFoundException("Appointment with ID " + id + " not found"));
        
        // Mark slot as available again
        AppointmentSlot slot = appointment.getAppointmentSlot();
        slot.setAvailable(true);
        appointmentSlotRepository.save(slot);
        
        appointmentRepository.deleteById(id);
        logger.info("Successfully deleted appointment with ID: {} and freed slot ID: {}", id, slot.getId());
        return true;
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        logger.debug("Fetching all appointments");
        List<AppointmentResponseDTO> result = appointmentRepository.findAll()
            .stream()
            .map(Mapper::toAppointmentResponseDTO)
            .collect(Collectors.toList());
        logger.debug("Successfully fetched {} appointments", result.size());
        return result;
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByUserId(String userId) {
        logger.debug("Fetching appointments for user ID: {}", userId);
        List<AppointmentResponseDTO> result = appointmentRepository.findByPatientId(userId)
            .stream()
            .map(Mapper::toAppointmentResponseDTO)
            .collect(Collectors.toList());
        logger.debug("Successfully fetched {} appointments for user ID: {}", result.size(), userId);
        return result;
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDoctorId(String doctorId) {
        logger.debug("Fetching appointments for doctor ID: {}", doctorId);
        List<AppointmentResponseDTO> result = appointmentRepository.findByDoctorId(doctorId)
            .stream()
            .map(Mapper::toAppointmentResponseDTO)
            .collect(Collectors.toList());
        logger.debug("Successfully fetched {} appointments for doctor ID: {}", result.size(), doctorId);
        return result;
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDate(LocalDate date) {
        logger.debug("Fetching appointments for date: {}", date);
        List<AppointmentResponseDTO> result = appointmentRepository.findByDate(date)
            .stream()
            .map(Mapper::toAppointmentResponseDTO)
            .collect(Collectors.toList());
        logger.debug("Successfully fetched {} appointments for date: {}", result.size(), date);
        return result;
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Fetching appointments from {} to {}", startDate, endDate);
        List<AppointmentResponseDTO> result = appointmentRepository.findByDateRange(startDate, endDate)
            .stream()
            .map(Mapper::toAppointmentResponseDTO)
            .collect(Collectors.toList());
        logger.debug("Successfully fetched {} appointments from {} to {}", result.size(), startDate, endDate);
        return result;
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByStatus(AppointmentStatus status) {
        logger.debug("Fetching appointments with status: {}", status);
        List<AppointmentResponseDTO> result = appointmentRepository.findByAppointmentStatus(status)
            .stream()
            .map(Mapper::toAppointmentResponseDTO)
            .collect(Collectors.toList());
        logger.debug("Successfully fetched {} appointments with status: {}", result.size(), status);
        return result;
    }
}
