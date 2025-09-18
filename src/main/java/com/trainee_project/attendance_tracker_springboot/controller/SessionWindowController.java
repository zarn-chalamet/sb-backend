package com.trainee_project.attendance_tracker_springboot.controller;

import com.trainee_project.attendance_tracker_springboot.dto.SessionWindowRequestDto;
import com.trainee_project.attendance_tracker_springboot.dto.SessionWindowResponseDto;
import com.trainee_project.attendance_tracker_springboot.service.SessionWindowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/api/sessions")
@RequiredArgsConstructor
public class SessionWindowController {

    private final SessionWindowService sessionWindowService;

    //only admin can
    //create session type
    @PostMapping()
    public ResponseEntity<SessionWindowResponseDto> createSession(@RequestBody SessionWindowRequestDto request) {

        SessionWindowResponseDto sessionWindow = sessionWindowService.createSession(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(sessionWindow);
    }

    //get session lists
    @GetMapping()
    public ResponseEntity<List<SessionWindowResponseDto>> getSessions() {

        List<SessionWindowResponseDto> sessions = sessionWindowService.getSessionList();

        return ResponseEntity.ok(sessions);
    }

    //get session type
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionWindowResponseDto> getSession(@PathVariable String sessionId) {

        SessionWindowResponseDto session = sessionWindowService.getSessionById(sessionId);

        return ResponseEntity.ok(session);
    }

    //update session
    @PutMapping("/{sessionId}")
    public ResponseEntity<SessionWindowResponseDto> updateSession(@PathVariable String sessionId,
                                                               @RequestBody SessionWindowRequestDto request) {

        SessionWindowResponseDto session = sessionWindowService.updateSessionById(sessionId,request);

        return ResponseEntity.ok(session);
    }

    //delete session
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable String sessionId) {

        sessionWindowService.deleteSessionById(sessionId);

        return ResponseEntity.ok("Deleted session successfully.");
    }

    //get all active sections
    @GetMapping("/active")
    public ResponseEntity<List<SessionWindowResponseDto>> getAllActiveSessions() {

        List<SessionWindowResponseDto> sessions = sessionWindowService.getAllActiveSessionList();

        return ResponseEntity.ok(sessions);
    }

}
