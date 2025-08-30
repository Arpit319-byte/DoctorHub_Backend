package com.example.DoctorHub.model;

import com.example.DoctorHub.Enum.AppointmentStatus;

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
public class Appointment extends BaseModel {

    @OneToOne
    @JoinColumn(name = "appointment_slot_id" , nullable = false)
    private AppointmentSlot appointmentSlot;

    @OneToOne
    @JoinColumn(name = "doctor_id" , nullable = false)
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "patient_id" , nullable = false)
    private User patient;

    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;
}
