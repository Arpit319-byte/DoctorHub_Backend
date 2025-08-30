package com.example.DoctorHub.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSlotResponseDTO {
    
    private String doctorId;
    private LocalDateTime startTime;
    private LocalDateTime endDateTime;
    private boolean isAvailable;
    
}
