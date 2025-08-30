package com.example.DoctorHub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.DoctorHub.dto.DoctorRequestDTO;
import com.example.DoctorHub.dto.DoctorResponseDTO;
import com.example.DoctorHub.service.IDoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    
    private final IDoctorService doctorService;

    public DoctorController(IDoctorService _doctorService) {
        logger.info("Initializing DoctorController with DoctorService");
        this.doctorService = _doctorService;
    }

   @PostMapping
   public ResponseEntity<DoctorResponseDTO> createDoctor(@RequestBody @Valid DoctorRequestDTO doctorRequestDTO) {
       logger.info("POST /api/v1/doctors - Creating new doctor with email: {}", doctorRequestDTO.getEmail());
       DoctorResponseDTO result = doctorService.createDoctor(doctorRequestDTO);
       logger.info("Successfully created doctor with ID: {}", result.getId());
       return ResponseEntity.status(HttpStatus.CREATED).body(result);
   }

   @GetMapping("/{id}")
   public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable String id) {
       logger.debug("GET /api/v1/doctors/{} - Fetching doctor by ID", id);
       DoctorResponseDTO result = doctorService.getDoctorById(id);
       logger.debug("Successfully fetched doctor with ID: {}", id);
       return ResponseEntity.ok(result);
   }

   @PutMapping("/{id}")
   public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable String id, @RequestBody @Valid DoctorRequestDTO doctorRequestDTO) {
       logger.info("PUT /api/v1/doctors/{} - Updating doctor", id);
       DoctorResponseDTO result = doctorService.updateDoctor(id, doctorRequestDTO);
       logger.info("Successfully updated doctor with ID: {}", id);
       return ResponseEntity.ok(result);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteDoctor(@PathVariable String id) {
       logger.info("DELETE /api/v1/doctors/{} - Deleting doctor", id);
       doctorService.deleteDoctor(id);
       logger.info("Successfully deleted doctor with ID: {}", id);
       return ResponseEntity.noContent().build();
   }
   
   @GetMapping
   public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
       logger.debug("GET /api/v1/doctors - Fetching all doctors");
       List<DoctorResponseDTO> result = doctorService.getAllDoctors();
       logger.debug("Successfully fetched {} doctors", result.size());
       return ResponseEntity.ok(result);
   }
   
   @GetMapping("/specialty/{specialty}")
   public ResponseEntity<List<DoctorResponseDTO>> getDoctorsBySpecialty(@PathVariable String specialty) {
       logger.debug("GET /api/v1/doctors/specialty/{} - Fetching doctors by specialty", specialty);
       List<DoctorResponseDTO> result = doctorService.getDoctorsBySpecialty(specialty);
       logger.debug("Successfully fetched {} doctors with specialty: {}", result.size(), specialty);
       return ResponseEntity.ok(result);
   }
   
   @GetMapping("/top-rated")
   public ResponseEntity<List<DoctorResponseDTO>> getTopRatedDoctors(@RequestParam(defaultValue = "10") int limit) {
       logger.debug("GET /api/v1/doctors/top-rated - Fetching top {} rated doctors", limit);
       List<DoctorResponseDTO> result = doctorService.getTopRatedDoctors(limit);
       logger.debug("Successfully fetched {} top rated doctors", result.size());
       return ResponseEntity.ok(result);
   }
}
