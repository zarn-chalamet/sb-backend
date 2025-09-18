package com.trainee_project.attendance_tracker_springboot.service;

import com.trainee_project.attendance_tracker_springboot.dto.OfficeLocationRequestDto;
import com.trainee_project.attendance_tracker_springboot.dto.OfficeLocationResponseDto;
import com.trainee_project.attendance_tracker_springboot.dto.UserResponseDto;

import java.util.List;

public interface OfficeLocationService {
    OfficeLocationResponseDto createOffice(OfficeLocationRequestDto request);

    List<OfficeLocationResponseDto> getOfficeLists();

    OfficeLocationResponseDto getOfficeById(String officeId);

    OfficeLocationResponseDto updateOfficeById(String officeId, OfficeLocationRequestDto request);

    void deleteOfficeById(String officeId);

    List<UserResponseDto> getUserListByOffice(String officeId);
}
