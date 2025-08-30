package com.example.DoctorHub.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DoctorHub.model.Appointment;
import com.example.DoctorHub.Enum.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    
    // Find appointments by user (patient)
    List<Appointment> findByPatientId(String patientId);
    
    // Find appointments by doctor
    List<Appointment> findByDoctorId(String doctorId);
    
    // Find appointments by status
    List<Appointment> findByAppointmentStatus(AppointmentStatus status);
    
    // Find appointments by date range
    @Query(value = "SELECT a.* FROM appointment a JOIN appointment_slot s ON a.appointment_slot_id = s.id WHERE DATE(s.start_time) BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Appointment> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find appointments by specific date
    @Query(value = "SELECT a.* FROM appointment a JOIN appointment_slot s ON a.appointment_slot_id = s.id WHERE DATE(s.start_time) = :date", nativeQuery = true)
    List<Appointment> findByDate(@Param("date") LocalDate date);
}
