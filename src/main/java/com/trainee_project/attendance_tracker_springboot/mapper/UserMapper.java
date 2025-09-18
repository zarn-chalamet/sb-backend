package com.trainee_project.attendance_tracker_springboot.mapper;

import com.trainee_project.attendance_tracker_springboot.dto.UserResponseDto;
import com.trainee_project.attendance_tracker_springboot.model.User;

public class UserMapper {
    public static UserResponseDto mapToDto(User user) {
        if(user == null) return null;

        return UserResponseDto.builder()
                .id(user.getId())
                .role(user.getRole().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .officeId(user.getAssignedOffice().getId())
                .office_name(user.getAssignedOffice().getName())
                .faceUrl(user.getFaceUrl())
                .build();
    }
}
