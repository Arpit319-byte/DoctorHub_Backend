package com.example.DoctorHub.dto;



import com.example.DoctorHub.Enum.AppointmentStatus;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    // Core Appointment Details

    @NotBlank(message = "AppointmentSlot Id is required")
    private String appointmentSlotId;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;
    
    @NotBlank(message = "Patient ID is required")
    private String patientId;
    
    private AppointmentStatus status = AppointmentStatus.PENDING;

}

