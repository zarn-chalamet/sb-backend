package com.trainee_project.attendance_tracker_springboot.controller;

import com.trainee_project.attendance_tracker_springboot.dto.LoginRequestDto;
import com.trainee_project.attendance_tracker_springboot.dto.OfficeLocationResponseDto;
import com.trainee_project.attendance_tracker_springboot.dto.RegisterRequestDto;
import com.trainee_project.attendance_tracker_springboot.security.jwt.JwtAuthResponse;
import com.trainee_project.attendance_tracker_springboot.service.AuthService;
import com.trainee_project.attendance_tracker_springboot.service.OfficeLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OfficeLocationService officeLocationService;

    //login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequestDto) {

        JwtAuthResponse jwtResponse = authService.authenticateUser(loginRequestDto);

        return ResponseEntity.ok(jwtResponse);
    }

    //register user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam("file") MultipartFile file,
                                          @RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("officeId") String officeId) throws IOException {
        System.out.println("register user ran.");
        authService.createUser(file, username, email, password, officeId);

        return ResponseEntity.status(HttpStatus.CREATED).body("Registered successfully.");
    }

    //get office list
    @GetMapping("/offices")
    public ResponseEntity<List<OfficeLocationResponseDto>> getOffices() {

        List<OfficeLocationResponseDto> offices = officeLocationService.getOfficeLists();

        return ResponseEntity.ok(offices);
    }
}
