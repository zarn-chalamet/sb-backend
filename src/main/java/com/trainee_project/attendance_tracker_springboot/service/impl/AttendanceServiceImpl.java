package com.trainee_project.attendance_tracker_springboot.service.impl;

import com.trainee_project.attendance_tracker_springboot.dto.*;
import com.trainee_project.attendance_tracker_springboot.exception.OfficeNotFoundException;
import com.trainee_project.attendance_tracker_springboot.exception.SessionWindowNotFoundException;
import com.trainee_project.attendance_tracker_springboot.model.*;
import com.trainee_project.attendance_tracker_springboot.repository.AttendanceRecordRepository;
import com.trainee_project.attendance_tracker_springboot.repository.OfficeLocationRepository;
import com.trainee_project.attendance_tracker_springboot.repository.SessionWindowRepository;
import com.trainee_project.attendance_tracker_springboot.repository.UserRepository;
import com.trainee_project.attendance_tracker_springboot.service.AttendanceService;
import com.trainee_project.attendance_tracker_springboot.service.FaceRecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final UserRepository userRepository;
    private final SessionWindowRepository sessionWindowRepository;
    private final FaceRecognitionService faceRecognitionService;
    private final OfficeLocationRepository officeLocationRepository;

    @Override
    @Transactional
    public AttendanceRecordDto clockIn(String email, MultipartFile file, SessionType sessionType, double lat, double lng) throws IOException {

        //get user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+email));

        //get office
        OfficeLocation office = user.getAssignedOffice();
        if (office == null) {
            throw new IllegalStateException("User has no assigned office location");
        }

        //get session window by session type
        SessionWindow session = sessionWindowRepository.findBySessionType(sessionType)
                .orElseThrow(() -> new SessionWindowNotFoundException("Session window not found with session type: "+sessionType));

        LocalTime nowTime = LocalTime.now();
        if(nowTime.isBefore(session.getStartTime()) || nowTime.isAfter(session.getEndTime())) {
            throw new IllegalArgumentException("Not in session time window.");
        }

        // prevent duplicate clock-in for same session & day
        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.atTime(LocalTime.MAX);


        Optional<AttendanceRecord> existingOpen = attendanceRecordRepository
                .findTopByUserAndSessionTypeAndClockInTimeBetweenOrderByClockInTimeDesc(
                        user, sessionType, dayStart, dayEnd);


        if (existingOpen.isPresent() && existingOpen.get().getClockOutTime() == null) {
            throw new IllegalStateException("Already clocked in for this session and not clocked out yet.");
        }

        //face check
        byte[] referenceImage = Files.readAllBytes(Paths.get(user.getFaceUrl()));
        byte[] liveImage = file.getBytes();

        FaceVerificationResponseDto faceResponse = faceRecognitionService.verifyFace(referenceImage, liveImage);

        if (!faceResponse.isVerified()) {
            throw new IllegalStateException("Face verification failed. Confidence: " + faceResponse.getConfidence());
        }

        //save
        AttendanceRecord record = AttendanceRecord.builder()
                .user(user)
                .sessionType(sessionType)
                .clockInTime(LocalDateTime.now())
                .clockInLat(lat)
                .clockInLng(lng)
                .faceMatchScore(faceResponse.getConfidence())
                .status("OK")
                .build();
        AttendanceRecord savedRecord = attendanceRecordRepository.save(record);

        return AttendanceRecordDto.builder()
                .id(savedRecord.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .officeName(office.getName())
                .sessionType(savedRecord.getSessionType().toString())
                .clockInTime(savedRecord.getClockInTime())
                .clockInLat(savedRecord.getClockInLat())
                .clockInLng(savedRecord.getClockInLng())
                .status(savedRecord.getStatus())
                .build();
    }

    @Override
    @Transactional
    public AttendanceRecordDto clockOut(String email, MultipartFile file, SessionType sessionType, double lat, double lng) throws IOException {

        //get user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+email));

        //get office
        OfficeLocation office = user.getAssignedOffice();
        if (office == null) {
            throw new IllegalStateException("User has no assigned office location");
        }

        //get session window by session type
        SessionWindow session = sessionWindowRepository.findBySessionType(sessionType)
                .orElseThrow(() -> new SessionWindowNotFoundException("Session window not found with session type: "+sessionType));

        LocalTime nowTime = LocalTime.now();
        if(nowTime.isBefore(session.getStartTime()) || nowTime.isAfter(session.getEndTime())) {
            throw new IllegalArgumentException("Not in session time window.");
        }

        //find the latest attendance record for this user/session today
        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.atTime(LocalTime.MAX);


        Optional<AttendanceRecord> existingOpt = attendanceRecordRepository
                .findTopByUserAndSessionTypeAndClockInTimeBetweenOrderByClockInTimeDesc(
                        user, sessionType, dayStart, dayEnd);


        if (existingOpt.isEmpty()) {
            throw new IllegalStateException("No clock-in record found for this session today.");
        }


        AttendanceRecord record = existingOpt.get();
        if (record.getClockOutTime() != null) {
            throw new IllegalStateException("Already clocked out for this session.");
        }

        //face check
        byte[] referenceImage = Files.readAllBytes(Paths.get(user.getFaceUrl()));
        byte[] liveImage = file.getBytes();

        FaceVerificationResponseDto faceResponse = faceRecognitionService.verifyFace(referenceImage, liveImage);

        if (!faceResponse.isVerified()) {
            throw new IllegalStateException("Face verification failed. Confidence: " + faceResponse.getConfidence());
        }

        // update record
        record.setClockOutTime(LocalDateTime.now());
        record.setClockOutLat(lat);
        record.setClockOutLng(lng);

        AttendanceRecord savedRecord = attendanceRecordRepository.save(record);

        return AttendanceRecordDto.builder()
                .id(savedRecord.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .officeName(office.getName())
                .sessionType(savedRecord.getSessionType().toString())
                .clockInTime(savedRecord.getClockInTime())
                .clockOutTime(savedRecord.getClockOutTime())
                .clockInLat(savedRecord.getClockInLat())
                .clockInLng(savedRecord.getClockInLng())
                .clockOutLat(savedRecord.getClockOutLat())
                .clockOutLng(savedRecord.getClockOutLng())
                .status(savedRecord.getStatus())
                .build();
    }

    @Override
    public LocationVerifyResponseDto verifyLocation(String email, LocationVerifyRequestDto request) {

        //get user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+email));

        //get office
        OfficeLocation office = user.getAssignedOffice();
        if (office == null) {
            throw new IllegalStateException("User has no assigned office location");
        }
        System.out.println("==================");
        System.out.println(office.getRadiusMeters());


        double distance = haversine(request.getLat(), request.getLng(),
                office.getLatitude(), office.getLongitude());

        return LocationVerifyResponseDto.builder()
                .withinRadius(distance <= office.getRadiusMeters())
                .radiusMeters(office.getRadiusMeters())
                .build();
    }

    @Override
    public List<AttendanceRecordDto> getUserReport(String userId) {

        UUID uuid = UUID.fromString(userId);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id:" + userId));

        OfficeLocation office = officeLocationRepository.findById(user.getAssignedOffice().getId())
                .orElseThrow(() -> new OfficeNotFoundException("Office not found with id: "+user.getAssignedOffice().getId()));

        List<AttendanceRecord> records = attendanceRecordRepository.findByUser(user);

        return records.stream()
                .map(r -> AttendanceRecordDto.builder()
                        .id(r.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .officeId(user.getAssignedOffice().getId().toString())
                        .officeName(user.getAssignedOffice().getName())
                        .officeLatitude(office.getLatitude())
                        .officeLongitude(office.getLongitude())
                        .sessionType(r.getSessionType().toString())
                        .clockInTime(r.getClockInTime())
                        .clockOutTime(r.getClockOutTime())
                        .clockInLat(r.getClockInLat())
                        .clockInLng(r.getClockInLng())
                        .clockOutLat(r.getClockOutLat())
                        .clockOutLng(r.getClockOutLng())
                        .status(r.getStatus())
                        .build())
                .toList();
    }

    @Override
    public OfficeReportDto getOfficeReport(String officeId) {

        OfficeLocation office = officeLocationRepository.findById(Long.valueOf(officeId))
                .orElseThrow(() -> new OfficeNotFoundException("Office not found with id: "+ officeId));

        List<User> users = userRepository.findByAssignedOffice(office);
        List<AttendanceRecord> records = attendanceRecordRepository.findByUserIn(users);

        long totalSessions = users.size() * sessionWindowRepository.count();
        long totalPresent = records.stream().filter(r -> "OK".equals(r.getStatus())).count();
        long totalAbsent = totalSessions - totalPresent;

        double attendancePercentage = totalSessions > 0 ? (double) totalPresent / totalSessions * 100 : 0;

        return OfficeReportDto.builder()
                .officeName(office.getName())
                .totalUsers((long) users.size())
                .totalSessions(totalSessions)
                .totalPresent(totalPresent)
                .totalAbsent(totalAbsent)
                .attendancePercentage(attendancePercentage)
                .build();
    }

    @Override
    public OfficeReportSummaryDto getOfficeSummaryReport() {

        List<OfficeLocation> offices = officeLocationRepository.findAll();

        int totalEmployees = 0;
        int totalPresent = 0;
        int totalAbsent = 0;
        int totalLate = 0;

        List<OfficeReportSummaryDto.OfficeAttendanceSummaryDto> summaries = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        for (OfficeLocation office : offices) {
            List<User> employees = userRepository.findByAssignedOffice(office);
            int officeEmployees = employees.size();

            //count present
            int present = attendanceRecordRepository
                    .countByUser_AssignedOffice_IdAndStatusAndClockInTimeBetween(
                            office.getId(), "OK", startOfDay, endOfDay);


            int absent = officeEmployees - present;

            totalEmployees += officeEmployees;
            totalPresent += present;
            totalAbsent += absent;

            summaries.add(
                    OfficeReportSummaryDto.OfficeAttendanceSummaryDto.builder()
                            .officeId(office.getId())
                            .officeName(office.getName())
                            .totalEmployees(officeEmployees)
                            .presentCount(present)
                            .absentCount(absent)
                            .attendanceRate((officeEmployees == 0) ? 0 : ((present) * 100.0 / officeEmployees))
                            .build()
            );
        }

        return OfficeReportSummaryDto.builder()
                .totalOffices(offices.size())
                .totalEmployees(totalEmployees)
                .totalPresentToday(totalPresent)
                .totalAbsentToday(totalAbsent)
                .officeSummaries(summaries)
                .build();
    }

    @Override
    public AttendanceRecordDto getCurrentActiveRecord(String email) {

        // Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Get current session based on current time
        LocalTime nowTime = LocalTime.now();
        Optional<SessionWindow> currentSessionOpt = sessionWindowRepository
                .findFirstByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(nowTime, nowTime);

        if (currentSessionOpt.isEmpty()) {
            return null; // no active session right now
        }

        SessionWindow currentSession = currentSessionOpt.get();

        // Find last record of user
        Optional<AttendanceRecord> optionalRecord = attendanceRecordRepository
                .findTopByUser_EmailAndSessionTypeOrderByClockInTimeDesc(email, currentSession.getSessionType());

        if (optionalRecord.isEmpty()) {
            return null; // no record found for current session
        }

        AttendanceRecord record = optionalRecord.get();

        // Check if the record is today
        LocalDate today = LocalDate.now();
        if (!record.getClockInTime().toLocalDate().isEqual(today)) {
            return null; // last record is not from today
        }

        // Build DTO
        return AttendanceRecordDto.builder()
                .id(record.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .officeName(user.getAssignedOffice() != null ? user.getAssignedOffice().getName() : null)
                .sessionType(record.getSessionType() != null ? record.getSessionType().name() : null)
                .clockInTime(record.getClockInTime())
                .clockOutTime(record.getClockOutTime())
                .clockInLat(record.getClockInLat())
                .clockInLng(record.getClockInLng())
                .clockOutLat(record.getClockOutLat())
                .clockOutLng(record.getClockOutLng())
                .status(record.getStatus())
                .build();
    }

    @Override
    public List<AttendanceRecordDto> getAttendanceRecordBySessionType(SessionType sessionType) {

        //find the attendance records by sectionType and desc
        List<AttendanceRecord> records = attendanceRecordRepository
                .findBySessionTypeOrderByClockInTimeDesc(sessionType);

        return records.stream()
                .map(record -> AttendanceRecordDto.builder()
                        .id(record.getId())
                        .username(record.getUser().getUsername())
                        .email(record.getUser().getEmail())
                        .officeId(record.getUser().getAssignedOffice().getId().toString())
                        .officeName(record.getUser().getAssignedOffice().getName())
                        .officeLatitude(record.getUser().getAssignedOffice().getLatitude())
                        .officeLongitude(record.getUser().getAssignedOffice().getLongitude())
                        .sessionType(record.getSessionType().toString())
                        .clockInTime(record.getClockInTime())
                        .clockOutTime(record.getClockOutTime())
                        .clockInLat(record.getClockInLat())
                        .clockInLng(record.getClockInLng())
                        .clockOutLat(record.getClockOutLat())
                        .clockOutLng(record.getClockOutLng())
                        .status(record.getStatus())
                        .build())
                .toList();
    }


    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // distance in meters
    }

}
