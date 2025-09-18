package com.trainee_project.attendance_tracker_springboot.service.impl;

import com.trainee_project.attendance_tracker_springboot.dto.LoginRequestDto;
import com.trainee_project.attendance_tracker_springboot.dto.RegisterRequestDto;
import com.trainee_project.attendance_tracker_springboot.exception.OfficeNotFoundException;
import com.trainee_project.attendance_tracker_springboot.exception.UserAlreadyExistException;
import com.trainee_project.attendance_tracker_springboot.model.OfficeLocation;
import com.trainee_project.attendance_tracker_springboot.model.Role;
import com.trainee_project.attendance_tracker_springboot.model.User;
import com.trainee_project.attendance_tracker_springboot.repository.OfficeLocationRepository;
import com.trainee_project.attendance_tracker_springboot.repository.UserRepository;
import com.trainee_project.attendance_tracker_springboot.security.UserDetailsImpl;
import com.trainee_project.attendance_tracker_springboot.security.jwt.JwtAuthResponse;
import com.trainee_project.attendance_tracker_springboot.security.jwt.JwtTokenProvider;
import com.trainee_project.attendance_tracker_springboot.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final OfficeLocationRepository officeLocationRepository;

    @Override
    public JwtAuthResponse authenticateUser(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(userDetails);

        return new JwtAuthResponse(jwt);

    }

    @Override
    public void createUser(MultipartFile file, String username, String email, String password, String officeId) throws IOException {

        System.out.println("---------------------------");
        System.out.println(username);
        System.out.println(password);
        System.out.println(officeId);
        //check the user exist or not
        boolean userExists = userRepository.existsByEmail(email);
        if(userExists) {
            throw new UserAlreadyExistException("User already registered with email: "+ email);
        }

        //get office by id
        OfficeLocation office = officeLocationRepository.findById(Long.valueOf(officeId))
                .orElseThrow(() -> new OfficeNotFoundException("Office not found with id: " + officeId));

        //save url in the upload file
        Path uploadPath = Paths.get("upload").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        //save new User
        User newUser = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .assignedOffice(office)
                .role(Role.USER_ROLE)
                .faceUrl(targetLocation.toString())
                .build();

        userRepository.save(newUser);

    }
}
