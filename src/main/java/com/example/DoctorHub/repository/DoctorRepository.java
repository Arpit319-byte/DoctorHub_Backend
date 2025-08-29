package com.example.DoctorHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.DoctorHub.model.Doctor;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    
    List<Doctor> findBySpecialization(String specialization);

    
    @Query(value = "SELECT * FROM doctor d ORDER BY CAST(d.rating AS DECIMAL(10,2)) DESC LIMIT :limit", nativeQuery = true)
    List<Doctor> findAllOrderByRatingDescAndLimit(@Param("limit") int limit);
}
