package com.trainee_project.attendance_tracker_springboot.service.impl;

import com.trainee_project.attendance_tracker_springboot.dto.UserResponseDto;
import com.trainee_project.attendance_tracker_springboot.mapper.UserMapper;
import com.trainee_project.attendance_tracker_springboot.model.User;
import com.trainee_project.attendance_tracker_springboot.repository.UserRepository;
import com.trainee_project.attendance_tracker_springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDto> getUserList(String email) {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::mapToDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(String userId) {

        UUID uuid = UUID.fromString(userId);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: "+userId));

        return UserMapper.mapToDto(user);
    }

    @Override
    public UserResponseDto updateFaceEmbeddingJson(String userId, MultipartFile file) throws IOException {

        UUID uuid = UUID.fromString(userId);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: "+userId));

        //save url in the upload file
        Path uploadPath = Paths.get("upload").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        user.setFaceUrl(targetLocation.toString());
        userRepository.save(user);

        return UserMapper.mapToDto(user);
    }

    @Override
    public UserResponseDto getUserProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+ email));

        return UserMapper.mapToDto(user);
    }
}
