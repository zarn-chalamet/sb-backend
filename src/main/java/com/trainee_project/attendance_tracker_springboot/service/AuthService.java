package com.trainee_project.attendance_tracker_springboot.service;

import com.trainee_project.attendance_tracker_springboot.dto.LoginRequestDto;
import com.trainee_project.attendance_tracker_springboot.dto.RegisterRequestDto;
import com.trainee_project.attendance_tracker_springboot.security.jwt.JwtAuthResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    
    JwtAuthResponse authenticateUser(LoginRequestDto loginRequestDto);


    void createUser(MultipartFile file, String username, String email, String password, String officeId) throws IOException;
}
