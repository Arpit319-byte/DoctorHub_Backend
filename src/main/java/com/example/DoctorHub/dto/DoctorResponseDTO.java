package com.example.DoctorHub.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    
    private String id;
    private String name;
    private String email;
    private String specialty;
    private String licenseNumber;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
