package com.trainee_project.attendance_tracker_springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attendance_records")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;

    private Double clockInLat;
    private Double clockInLng;
    private Double clockOutLat;
    private Double clockOutLng;

    private Double faceMatchScore; // similarity/confidence from face service
    private String status; // "OK", "LATE", "FACE_MISMATCH"
}
