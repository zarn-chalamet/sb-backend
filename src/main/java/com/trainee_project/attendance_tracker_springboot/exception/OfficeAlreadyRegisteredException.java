package com.trainee_project.attendance_tracker_springboot.exception;

public class OfficeAlreadyRegisteredException extends RuntimeException{
    public OfficeAlreadyRegisteredException(String message) {
        super(message);
    }
}
