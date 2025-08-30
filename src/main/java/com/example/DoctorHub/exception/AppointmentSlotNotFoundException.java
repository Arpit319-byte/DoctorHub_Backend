package com.example.DoctorHub.exception;

public class AppointmentSlotNotFoundException extends RuntimeException {

    public AppointmentSlotNotFoundException(String message) {
        super(message);
    }

    public AppointmentSlotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
