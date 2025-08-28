package com.example.DoctorHub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import com.example.DoctorHub.service.IDoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final IDoctorService doctorService;

    public DoctorController(IDoctorService _doctorService) {
        this.doctorService = _doctorService;
    }

   @PostMapping
   public ResponseEntity<DoctorResponseDTO> createDoctor(@RequestBody @Valid DoctorRequestDTO doctorRequestDTO) {
       return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctorRequestDTO));
   }

   @GetMapping("/{id}")
   public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable String id) {
    return ResponseEntity.ok(doctorService.getDoctorById(id));
   }

   @PutMapping("/{id}")
   public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable String id, @RequestBody @Valid DoctorRequestDTO doctorRequestDTO) {
    return ResponseEntity.ok(doctorService.updateDoctor(id, doctorRequestDTO));
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteDoctor(@PathVariable String id) {
    doctorService.deleteDoctor(id);
    return ResponseEntity.noContent().build();
   }
}
