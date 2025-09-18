package com.trainee_project.attendance_tracker_springboot.repository;

import com.trainee_project.attendance_tracker_springboot.model.AttendanceRecord;
import com.trainee_project.attendance_tracker_springboot.model.SessionType;
import com.trainee_project.attendance_tracker_springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, String> {
    Optional<AttendanceRecord> findTopByUserAndSessionTypeAndClockInTimeBetweenOrderByClockInTimeDesc(User user, SessionType sessionType, LocalDateTime dayStart, LocalDateTime dayEnd);

    List<AttendanceRecord> findByUser(User users);

    List<AttendanceRecord> findByUserIn(List<User> users);

    int countByUser_AssignedOffice_IdAndStatusAndClockInTimeBetween(Long id, String ok, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<AttendanceRecord> findTopByUser_EmailOrderByClockInTimeDesc(String email);

    Optional<AttendanceRecord> findTopByUser_EmailAndSessionTypeOrderByClockInTimeDesc(String email, SessionType sessionType);

    List<AttendanceRecord> findBySessionTypeOrderByClockInTimeDesc(SessionType sessionType);
}
