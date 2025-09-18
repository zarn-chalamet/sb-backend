package com.trainee_project.attendance_tracker_springboot.controller;

import com.trainee_project.attendance_tracker_springboot.dto.*;
import com.trainee_project.attendance_tracker_springboot.model.SessionType;
import com.trainee_project.attendance_tracker_springboot.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/clock-in")
    public ResponseEntity<AttendanceRecordDto> clockIn(@RequestParam("file")MultipartFile file,
                                                       @RequestParam SessionType sessionType,
                                                       @RequestParam double lat,
                                                       @RequestParam double lng,
                                                       Principal principal) throws IOException {

        String email = principal.getName();
        AttendanceRecordDto record = attendanceService.clockIn(email, file,sessionType, lat,lng);

        return ResponseEntity.ok(record);
    }

    @PostMapping("/clock-out")
    public ResponseEntity<AttendanceRecordDto> clockOut(@RequestParam("file")MultipartFile file,
                                                        @RequestParam SessionType sessionType,
                                                        @RequestParam double lat,
                                                        @RequestParam double lng,
                                                        Principal principal) throws IOException {

        String email = principal.getName();
        AttendanceRecordDto record = attendanceService.clockOut(email, file,sessionType, lat,lng);

        return ResponseEntity.ok(record);
    }

    //verify location according to the user's office location(200 meter)
    @PostMapping("/verify")
    public ResponseEntity<LocationVerifyResponseDto> verifyLocation(@RequestBody LocationVerifyRequestDto request,
                                                                    Principal principal) {
        String email = principal.getName();
        LocationVerifyResponseDto verification = attendanceService.verifyLocation(email, request);

        return ResponseEntity.ok(verification);
    }

    //get attendance list by user
    @GetMapping("/users/{userId}/report")
    public ResponseEntity<List<AttendanceRecordDto>> getUserReports(@PathVariable String userId) {

        List<AttendanceRecordDto> reports = attendanceService.getUserReport(userId);
        return ResponseEntity.ok(reports);
    }

    //get attendance report by office
    @GetMapping("/offices/{officeId}/report")
    public ResponseEntity<OfficeReportDto> getOfficeReports(@PathVariable String officeId) {

        OfficeReportDto report = attendanceService.getOfficeReport(officeId);

        return ResponseEntity.ok(report);
    }

    //get office report(all offices)
    @GetMapping("/offices/report")
    public ResponseEntity<OfficeReportSummaryDto> getOfficeSummaryReport() {

        OfficeReportSummaryDto report = attendanceService.getOfficeSummaryReport();

        return ResponseEntity.ok(report);
    }

    //get current active section of user
    @GetMapping("/current")
    public ResponseEntity<AttendanceRecordDto> getCurrentActiveAttendanceRecord(Principal principal) {

        String email = principal.getName();
        AttendanceRecordDto record = attendanceService.getCurrentActiveRecord(email);

        return ResponseEntity.ok(record);
    }

    //get attendance list by section type
    @GetMapping("/sessions")
    public ResponseEntity<List<AttendanceRecordDto>> getAttendanceRecordBySessionType(@RequestParam SessionType sessionType) {

        List<AttendanceRecordDto> records = attendanceService.getAttendanceRecordBySessionType(sessionType);

        return ResponseEntity.ok(records);
    }
}
