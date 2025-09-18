package com.trainee_project.attendance_tracker_springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfficeReportSummaryDto {
    private int totalOffices;
    private int totalEmployees;
    private int totalPresentToday;
    private int totalAbsentToday;

    private List<OfficeAttendanceSummaryDto> officeSummaries;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OfficeAttendanceSummaryDto {
        private Long officeId;
        private String officeName;
        private int totalEmployees;
        private int presentCount;
        private int absentCount;
        private double attendanceRate;
    }

}

