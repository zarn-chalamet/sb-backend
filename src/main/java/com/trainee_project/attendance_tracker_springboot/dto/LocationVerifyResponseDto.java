package com.trainee_project.attendance_tracker_springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationVerifyResponseDto {
    private boolean withinRadius;
    private int radiusMeters;
}
