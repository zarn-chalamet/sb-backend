package com.trainee_project.attendance_tracker_springboot.service;

import com.trainee_project.attendance_tracker_springboot.dto.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<UserResponseDto> getUserList(String email);

    UserResponseDto getUserById(String userId);

    UserResponseDto updateFaceEmbeddingJson(String userId, MultipartFile file) throws IOException;

    UserResponseDto getUserProfile(String email);
}
