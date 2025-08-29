package com.example.DoctorHub.service;

import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import java.util.List;

public interface IDoctorService {
    
    /**
     * Create a new doctor
     */
    DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO);
    
    /**
     * Get doctor by ID
     */
    DoctorResponseDTO getDoctorById(String id);
    
    /**
     * Update existing doctor
     */
    DoctorResponseDTO updateDoctor(String id, DoctorRequestDTO doctorRequestDTO);
    
    /**
     * Delete doctor by ID
     */
    void deleteDoctor(String id);
    
    /**
     * Get all doctors
     */
    List<DoctorResponseDTO> getAllDoctors();
    
    /**
     * Get doctors by specialty
     */
    List<DoctorResponseDTO> getDoctorsBySpecialty(String specialty);
    
    // /**
    //  * Get doctors by location
    //  */
    // List<DoctorResponseDTO> getDoctorsByLocation(String location);
    
    /**
     * Get available doctors for a specific date and time
     */
    List<DoctorResponseDTO> getAvailableDoctors(String date, String time);
    
    /**
     * Verify doctor license
     */
    boolean verifyDoctorLicense(String doctorId, String licenseNumber);
    
    /**
     * Update doctor availability
     */
    void updateDoctorAvailability(String doctorId, String schedule);
    
    /**
     * Get doctor rating
     */
    double getDoctorRating(String doctorId);
    
    /**
     * Get top rated doctors
     */
    List<DoctorResponseDTO> getTopRatedDoctors(int limit);
}
