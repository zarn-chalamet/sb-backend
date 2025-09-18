package com.trainee_project.attendance_tracker_springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRecordDto {

    private UUID id;

    private String username;

    private String email;

    private String officeId;

    private String officeName;

    private double officeLatitude;

    private double officeLongitude;

    private String sessionType;

    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;

    private Double clockInLat;
    private Double clockInLng;
    private Double clockOutLat;
    private Double clockOutLng;

    private String status;
}
