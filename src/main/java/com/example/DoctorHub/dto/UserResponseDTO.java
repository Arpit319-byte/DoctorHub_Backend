package com.example.DoctorHub.dto;

import com.example.DoctorHub.Enum.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    
    private String id;
    private String name;
    private String email;
    private Role role;
}
