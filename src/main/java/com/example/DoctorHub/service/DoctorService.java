package com.example.DoctorHub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import com.example.DoctorHub.mapper.Mapper;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.repository.DoctorRepository;
import com.example.DoctorHub.exception.DoctorNotFoundException;

@Service
public class DoctorService implements IDoctorService{

    private static final Logger logger = LoggerFactory.getLogger(DoctorService.class);
    
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository _doctorRepository){
        logger.info("Initializing DoctorService with DoctorRepository");
        this.doctorRepository =_doctorRepository;
    }

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        logger.info("Creating new doctor with email: {}", doctorRequestDTO.getEmail());
        DoctorResponseDTO result = Mapper.toDoctorResponseDTO(doctorRepository.save(Mapper.toDoctor(doctorRequestDTO)));
        logger.info("Successfully created doctor with ID: {}", result.getId());
        return result;
    }

    @Override
    public DoctorResponseDTO getDoctorById(String id) {
        logger.debug("Fetching doctor by ID: {}", id);
        DoctorResponseDTO result = Mapper.toDoctorResponseDTO(doctorRepository.findById(id)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + id + " not found")));
        logger.debug("Successfully fetched doctor with ID: {}", id);
        return result;
    }

    @Override
    public DoctorResponseDTO updateDoctor(String id, DoctorRequestDTO doctorRequestDTO) {
        logger.info("Updating doctor with ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + id + " not found"));
        doctor.setSpecialization(doctorRequestDTO.getSpecialty());
        doctor.setLicenseNumber(doctorRequestDTO.getLicenseNumber());
        doctor.setRating(doctorRequestDTO.getRating());
        DoctorResponseDTO result = Mapper.toDoctorResponseDTO(doctorRepository.save(doctor));
        logger.info("Successfully updated doctor with ID: {}", id);
        return result;
    }

    @Override
    public boolean deleteDoctor(String id) {
        logger.info("Deleting doctor with ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + id + " not found"));
        doctorRepository.deleteById(id);
        logger.info("Successfully deleted doctor with ID: {}", id);
        return true;
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        logger.debug("Fetching all doctors");
        List<DoctorResponseDTO> result = doctorRepository.findAll().stream().map(Mapper::toDoctorResponseDTO).collect(Collectors.toList());
        logger.debug("Successfully fetched {} doctors", result.size());
        return result;
    }

    @Override
    public List<DoctorResponseDTO> getDoctorsBySpecialty(String specialty) {
        logger.debug("Fetching doctors by specialty: {}", specialty);
        List<DoctorResponseDTO> result = doctorRepository.findBySpecialization(specialty).stream().map(Mapper::toDoctorResponseDTO).collect(Collectors.toList());
        logger.debug("Successfully fetched {} doctors with specialty: {}", result.size(), specialty);
        return result;
    }

    @Override
    public List<DoctorResponseDTO> getAvailableDoctors(String date, String time) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableDoctors'");
    }

    @Override
    public boolean verifyDoctorLicense(String doctorId, String licenseNumber) {
        logger.debug("Verifying license for doctor ID: {} with license number: {}", doctorId, licenseNumber);
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + doctorId + " not found"));
        
        boolean isValid = doctor.getLicenseNumber().equals(licenseNumber);
        logger.debug("License verification result for doctor ID {}: {}", doctorId, isValid);
        return isValid;
    }

    @Override
    public void updateDoctorAvailability(String doctorId, String schedule) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDoctorAvailability'");
    }

    @Override
    public double getDoctorRating(String doctorId) {
        logger.debug("Fetching rating for doctor ID: {}", doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + doctorId + " not found"));
        double rating = doctor.getRating();
        logger.debug("Rating for doctor ID {}: {}", doctorId, rating);
        return rating;
    }

    @Override
    public List<DoctorResponseDTO> getTopRatedDoctors(int limit) {
        logger.debug("Fetching top {} rated doctors", limit);
        List<DoctorResponseDTO> result = doctorRepository.findAllOrderByRatingDescAndLimit(limit)
            .stream()
            .map(Mapper::toDoctorResponseDTO)
            .collect(Collectors.toList());
        logger.debug("Successfully fetched {} top rated doctors", result.size());
        return result;
    }

    
    
}
