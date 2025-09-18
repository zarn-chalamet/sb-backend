package com.trainee_project.attendance_tracker_springboot.dto;

import com.trainee_project.attendance_tracker_springboot.model.SessionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClockRequestDto {
    private SessionType sessionType;
    private double lat;
    private double lng;
}
