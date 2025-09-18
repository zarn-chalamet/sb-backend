package com.trainee_project.attendance_tracker_springboot.repository;

import com.trainee_project.attendance_tracker_springboot.model.SessionType;
import com.trainee_project.attendance_tracker_springboot.model.SessionWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface SessionWindowRepository extends JpaRepository<SessionWindow, String> {
    boolean existsBySessionType(SessionType sessionType);

    Optional<SessionWindow> findBySessionType(SessionType sessionType);

    Optional<SessionWindow> findFirstByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(LocalTime nowTime, LocalTime nowTime1);
}
