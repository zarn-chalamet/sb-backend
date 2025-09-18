package com.trainee_project.attendance_tracker_springboot.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {

    private String username;

    private String email;

    private String password;

    private String officeId;
}
