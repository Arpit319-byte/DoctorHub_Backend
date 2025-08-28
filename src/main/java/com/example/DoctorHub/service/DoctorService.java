package com.example.DoctorHub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;

@Service
public class DoctorService implements IDoctorService{

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createDoctor'");
    }

    @Override
    public DoctorResponseDTO getDoctorById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDoctorById'");
    }

    @Override
    public DoctorResponseDTO updateDoctor(String id, DoctorRequestDTO doctorRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDoctor'");
    }

    @Override
    public void deleteDoctor(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteDoctor'");
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllDoctors'");
    }

    @Override
    public List<DoctorResponseDTO> getDoctorsBySpecialty(String specialty) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDoctorsBySpecialty'");
    }

    @Override
    public List<DoctorResponseDTO> getDoctorsByLocation(String location) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDoctorsByLocation'");
    }

    @Override
    public List<DoctorResponseDTO> getAvailableDoctors(String date, String time) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableDoctors'");
    }

    @Override
    public void verifyDoctorLicense(String doctorId, String licenseNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyDoctorLicense'");
    }

    @Override
    public void updateDoctorAvailability(String doctorId, String schedule) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDoctorAvailability'");
    }

    @Override
    public double getDoctorRating(String doctorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDoctorRating'");
    }

    @Override
    public List<DoctorResponseDTO> getTopRatedDoctors(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopRatedDoctors'");
    }

    
    
}
