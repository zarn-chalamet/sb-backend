package com.trainee_project.attendance_tracker_springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private UUID id;

    private String role;

    private String username;

    private String email;

    private String faceUrl;

    private Long officeId;

    private String office_name;
}
