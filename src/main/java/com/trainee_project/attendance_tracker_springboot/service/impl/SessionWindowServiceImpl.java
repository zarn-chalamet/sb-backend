package com.trainee_project.attendance_tracker_springboot.service.impl;

import com.trainee_project.attendance_tracker_springboot.dto.SessionWindowRequestDto;
import com.trainee_project.attendance_tracker_springboot.dto.SessionWindowResponseDto;
import com.trainee_project.attendance_tracker_springboot.exception.SessionAlreadyExistException;
import com.trainee_project.attendance_tracker_springboot.exception.SessionWindowNotFoundException;
import com.trainee_project.attendance_tracker_springboot.mapper.SessionWindowMapper;
import com.trainee_project.attendance_tracker_springboot.model.OfficeLocation;
import com.trainee_project.attendance_tracker_springboot.model.SessionWindow;
import com.trainee_project.attendance_tracker_springboot.repository.OfficeLocationRepository;
import com.trainee_project.attendance_tracker_springboot.repository.SessionWindowRepository;
import com.trainee_project.attendance_tracker_springboot.service.SessionWindowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionWindowServiceImpl implements SessionWindowService {

    private final SessionWindowRepository sessionWindowRepository;
    private final OfficeLocationRepository officeLocationRepository;

    @Override
    public SessionWindowResponseDto createSession(SessionWindowRequestDto request) {

        //check session is already exist or not
        boolean sessionExists =  sessionWindowRepository.existsBySessionType(request.getSessionType());
        if(sessionExists) {
            throw new SessionAlreadyExistException("Session already existed by type: "+ request.getSessionType());
        }

        //save session to database
        SessionWindow session = SessionWindow.builder()
                .sessionType(request.getSessionType())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        SessionWindow createdSession = sessionWindowRepository.save(session);

        return SessionWindowMapper.mapToDto(createdSession);
    }

    @Override
    public List<SessionWindowResponseDto> getSessionList() {

        List<SessionWindow> sessions = sessionWindowRepository.findAll();

        return sessions.stream()
                .map(SessionWindowMapper::mapToDto)
                .toList();
    }

    @Override
    public SessionWindowResponseDto getSessionById(String sessionId) {

        SessionWindow session = sessionWindowRepository.findById(sessionId)
                .orElseThrow(() -> new SessionWindowNotFoundException("Session window not found with id: "+ sessionId));

        return SessionWindowMapper.mapToDto(session);
    }

    @Override
    public SessionWindowResponseDto updateSessionById(String sessionId, SessionWindowRequestDto request) {

        SessionWindow session = sessionWindowRepository.findById(sessionId)
                .orElseThrow(() -> new SessionWindowNotFoundException("Session window not found with id: "+ sessionId));

        if(request.getStartTime() != null) {
            session.setStartTime(request.getStartTime());
        }

        if(request.getEndTime() != null) {
            session.setEndTime(request.getEndTime());
        }
        sessionWindowRepository.save(session);

        return SessionWindowMapper.mapToDto(session);
    }

    @Override
    public void deleteSessionById(String sessionId) {

        SessionWindow session = sessionWindowRepository.findById(sessionId)
                .orElseThrow(() -> new SessionWindowNotFoundException("Session window not found with id: "+ sessionId));

        sessionWindowRepository.delete(session);
    }

    @Override
    public List<SessionWindowResponseDto> getAllActiveSessionList() {

        LocalTime now = LocalTime.now();

        List<SessionWindow> activeSessions = sessionWindowRepository.findAll().stream()
                .filter(session -> session.getStartTime() != null
                        && session.getEndTime() != null
                        && !now.isBefore(session.getStartTime())   // now >= startTime
                        && !now.isAfter(session.getEndTime()))     // now <= endTime
                .toList();

        return activeSessions.stream()
                .map(SessionWindowMapper::mapToDto) // convert entity → DTO
                .toList();
    }

}
