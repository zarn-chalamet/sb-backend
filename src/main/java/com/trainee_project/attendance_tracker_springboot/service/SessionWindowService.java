package com.trainee_project.attendance_tracker_springboot.service;

import com.trainee_project.attendance_tracker_springboot.dto.SessionWindowRequestDto;
import com.trainee_project.attendance_tracker_springboot.dto.SessionWindowResponseDto;

import java.util.List;

public interface SessionWindowService {
    SessionWindowResponseDto createSession(SessionWindowRequestDto request);

    List<SessionWindowResponseDto> getSessionList();

    SessionWindowResponseDto getSessionById(String sessionId);

    SessionWindowResponseDto updateSessionById(String sessionId, SessionWindowRequestDto request);

    void deleteSessionById(String sessionId);

    List<SessionWindowResponseDto> getAllActiveSessionList();
}
