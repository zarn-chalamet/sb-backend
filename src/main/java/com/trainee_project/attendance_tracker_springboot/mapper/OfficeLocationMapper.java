package com.trainee_project.attendance_tracker_springboot.mapper;

import com.trainee_project.attendance_tracker_springboot.dto.OfficeLocationResponseDto;
import com.trainee_project.attendance_tracker_springboot.model.OfficeLocation;

public class OfficeLocationMapper {

    public static OfficeLocationResponseDto mapToDto(OfficeLocation office) {

        if(office == null) return null;

        return OfficeLocationResponseDto.builder()
                .id(office.getId())
                .name(office.getName())
                .latitude(office.getLatitude())
                .longitude(office.getLongitude())
                .build();
    }
}
