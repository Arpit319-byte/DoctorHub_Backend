package com.example.DoctorHub.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.DoctorHub.dto.ErrorResponseDTO;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleDoctorNotFoundException() {
        DoctorNotFoundException ex = new DoctorNotFoundException("Doctor not found");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleDoctorNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Doctor Not Found", errorResponse.getError());
        assertEquals("Doctor not found", errorResponse.getMessage());
        assertEquals("/api/doctors", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleUserNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("User Not Found", errorResponse.getError());
        assertEquals("User not found", errorResponse.getMessage());
        assertEquals("/api/users", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleAppointmentNotFoundException() {
        AppointmentNotFoundException ex = new AppointmentNotFoundException("Appointment not found");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleAppointmentNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Appointment Not Found", errorResponse.getError());
        assertEquals("Appointment not found", errorResponse.getMessage());
        assertEquals("/api/appointments", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleAppointmentSlotNotFoundException() {
        AppointmentSlotNotFoundException ex = new AppointmentSlotNotFoundException("Appointment slot not found");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleAppointmentSlotNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Appointment Slot Not Found", errorResponse.getError());
        assertEquals("Appointment slot not found", errorResponse.getMessage());
        assertEquals("/api/appointment-slots", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleRuntimeException() {
        RuntimeException ex = new RuntimeException("Something went wrong");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleRuntimeException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        ErrorResponseDTO errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("Something went wrong", errorResponse.getMessage());
        assertEquals("/api/doctors", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void errorResponseDTOConstructor() {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(404, "Not Found", "Resource not found", "/api/test");

        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Resource not found", errorResponse.getMessage());
        assertEquals("/api/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void errorResponseDTONoArgsConstructor() {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        
        assertNotNull(errorResponse);
        // Default values should be null/0
        assertEquals(0, errorResponse.getStatus());
        assertNull(errorResponse.getError());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getPath());
        assertNull(errorResponse.getTimestamp());
    }
}
