package com.trainee_project.attendance_tracker_springboot.service;

import com.trainee_project.attendance_tracker_springboot.dto.*;
import com.trainee_project.attendance_tracker_springboot.model.SessionType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttendanceService {

    AttendanceRecordDto clockIn(String email, MultipartFile file, SessionType sessionType, double lat, double lng) throws IOException;

    AttendanceRecordDto clockOut(String email, MultipartFile file, SessionType sessionType, double lat, double lng) throws IOException;

    LocationVerifyResponseDto verifyLocation(String email, LocationVerifyRequestDto request);

    List<AttendanceRecordDto> getUserReport(String userId);

    OfficeReportDto getOfficeReport(String officeId);

    OfficeReportSummaryDto getOfficeSummaryReport();

    AttendanceRecordDto getCurrentActiveRecord(String email);

    List<AttendanceRecordDto> getAttendanceRecordBySessionType(SessionType sessionType);
}
