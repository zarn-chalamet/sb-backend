package com.trainee_project.attendance_tracker_springboot.dto;

import com.trainee_project.attendance_tracker_springboot.model.SessionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionWindowRequestDto {

    private SessionType sessionType;
    private LocalTime startTime;
    private LocalTime endTime;
}
