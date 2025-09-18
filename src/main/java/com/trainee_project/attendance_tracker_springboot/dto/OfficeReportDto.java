package com.trainee_project.attendance_tracker_springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeReportDto {

    private String officeName;
    private Long totalUsers;
    private Long totalSessions;
    private Long totalPresent;
    private Long totalAbsent;
    private Double attendancePercentage;

}
