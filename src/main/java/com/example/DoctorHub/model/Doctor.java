package com.example.DoctorHub.model;

import jakarta.persistence.CascadeType;
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
public class Doctor extends BaseModel {
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id" , nullable = false,unique = true)
    private User user;

    @Column(name = "specialization" , nullable = false)
    private String specialization;

    @Column(name = "license_number" , nullable = false)
    private String licenseNumber;

    @Column(name = "rating")
    private String rating;
    
}
