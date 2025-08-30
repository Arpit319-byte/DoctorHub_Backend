package com.example.DoctorHub.Enum;

public enum AppointmentStatus {
    SCHEDULED,      // Appointment is scheduled but not confirmed
    CONFIRMED,      // Patient has confirmed the appointment
    IN_PROGRESS,    // Appointment is currently happening
    COMPLETED,      // Appointment has been completed
    CANCELLED,      // Appointment was cancelled
    NO_SHOW,        // Patient didn't show up
    RESCHEDULED,    // Appointment was rescheduled
    PENDING,        // Waiting for confirmation
    URGENT,         // Emergency/urgent appointment
    ON_HOLD         // Temporarily on hold
}
