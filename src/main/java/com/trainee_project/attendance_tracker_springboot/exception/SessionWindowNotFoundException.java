package com.trainee_project.attendance_tracker_springboot.exception;

public class SessionWindowNotFoundException extends RuntimeException{
    public SessionWindowNotFoundException(String message) {
        super(message);
    }
}
