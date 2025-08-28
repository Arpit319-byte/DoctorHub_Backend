package com.example.DoctorHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.DoctorHub.model.Doctor;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    
    List<Doctor> findBySpecialization(String specialization);
}
