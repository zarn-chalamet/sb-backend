package com.trainee_project.attendance_tracker_springboot.mapper;

import com.trainee_project.attendance_tracker_springboot.dto.SessionWindowResponseDto;
import com.trainee_project.attendance_tracker_springboot.model.SessionWindow;

public class SessionWindowMapper {

    public static SessionWindowResponseDto mapToDto(SessionWindow session) {

        if(session == null) return null;

        return SessionWindowResponseDto.builder()
                .id(session.getId())
                .sessionType(session.getSessionType())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .build();
    }
}
