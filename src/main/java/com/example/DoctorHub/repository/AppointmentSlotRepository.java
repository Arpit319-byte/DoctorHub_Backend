package com.example.DoctorHub.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DoctorHub.model.AppointmentSlot;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, String> {
    
    // Find slots by doctor
    List<AppointmentSlot> findByDoctorId(String doctorId);
    
    // Find slots by doctor and date
    @Query(value = "SELECT s.* FROM appointment_slot s WHERE s.doctor_id = :doctorId AND DATE(s.start_time) = :date", nativeQuery = true)
    List<AppointmentSlot> findByDoctorIdAndDate(@Param("doctorId") String doctorId, @Param("date") LocalDate date);
    
    // Find available slots by doctor and date
    @Query(value = "SELECT s.* FROM appointment_slot s WHERE s.doctor_id = :doctorId AND DATE(s.start_time) = :date AND s.is_available = true", nativeQuery = true)
    List<AppointmentSlot> findAvailableSlotsByDoctorAndDate(@Param("doctorId") String doctorId, @Param("date") LocalDate date);
    
    // Find slots by doctor, date, and time range
    @Query(value = "SELECT s.* FROM appointment_slot s WHERE s.doctor_id = :doctorId AND DATE(s.start_time) = :date AND s.start_time >= :startTime AND s.end_time <= :endTime", nativeQuery = true)
    List<AppointmentSlot> findByDoctorIdAndDateAndTimeRange(@Param("doctorId") String doctorId, @Param("date") LocalDate date, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    // Find available slots
    List<AppointmentSlot> findByIsAvailableTrue();
}
