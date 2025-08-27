package com.example.DoctorHub.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class AppointmentSlot extends BaseModel {
 
    @OneToOne
    @JoinColumn(name = "doctor_id" , nullable = false)
    private Doctor doctor;

    @Column(name = "start_time" , nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time" , nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_available" , nullable = false)
    private boolean isAvailable;

    
}