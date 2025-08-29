package com.example.DoctorHub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import com.example.DoctorHub.mapper.Mapper;
import com.example.DoctorHub.model.Doctor;
import com.example.DoctorHub.repository.DoctorRepository;
import com.example.DoctorHub.exception.DoctorNotFoundException;

@Service
public class DoctorService implements IDoctorService{

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository _doctorRepository){
        this.doctorRepository =_doctorRepository;
    }

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        return Mapper.toDoctorResponseDTO(doctorRepository.save(Mapper.toDoctor(doctorRequestDTO)));
    }

    @Override
    public DoctorResponseDTO getDoctorById(String id) {
        return Mapper.toDoctorResponseDTO(doctorRepository.findById(id)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + id + " not found")));
    }

    @Override
    public DoctorResponseDTO updateDoctor(String id, DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + id + " not found"));
        doctor.setSpecialization(doctorRequestDTO.getSpecialty());
        doctor.setLicenseNumber(doctorRequestDTO.getLicenseNumber());
        doctor.setRating(doctorRequestDTO.getRating());
        return Mapper.toDoctorResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
        return;
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(Mapper::toDoctorResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDTO> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialization(specialty).stream().map(Mapper::toDoctorResponseDTO).collect(Collectors.toList());
    }

    // @Override
    // public List<DoctorResponseDTO> getDoctorsByLocation(String location) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getDoctorsByLocation'");
    // }

    @Override
    public List<DoctorResponseDTO> getAvailableDoctors(String date, String time) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableDoctors'");
    }

    @Override
    public boolean verifyDoctorLicense(String doctorId, String licenseNumber) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + doctorId + " not found"));
        
        if (!doctor.getLicenseNumber().equals(licenseNumber)) {
            return false;
        }
        return true;
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
