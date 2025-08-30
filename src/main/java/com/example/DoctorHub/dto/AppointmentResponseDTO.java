package com.example.DoctorHub.dto;

import com.example.DoctorHub.Enum.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {

    private String appointmentSlotId;
    private String doctorId;
    private String patientId;
    private AppointmentStatus appointmentStatus;
    
}
