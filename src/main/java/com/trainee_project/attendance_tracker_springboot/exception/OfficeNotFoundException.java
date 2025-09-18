package com.trainee_project.attendance_tracker_springboot.exception;

public class OfficeNotFoundException extends RuntimeException{
    public OfficeNotFoundException(String message) {
        super(message);
    }
}
