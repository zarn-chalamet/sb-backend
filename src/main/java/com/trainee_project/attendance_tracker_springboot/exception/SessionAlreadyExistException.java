package com.trainee_project.attendance_tracker_springboot.exception;

public class SessionAlreadyExistException extends RuntimeException{
    public SessionAlreadyExistException(String message) {
        super(message);
    }
}
