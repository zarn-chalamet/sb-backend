package com.trainee_project.attendance_tracker_springboot.controller;

import com.trainee_project.attendance_tracker_springboot.dto.UserResponseDto;
import com.trainee_project.attendance_tracker_springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //get user lists except from current authorized user (only admin can)
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsers(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(userService.getUserList(email));
    }

    //get user profile
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserData(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(userService.getUserProfile(email));
    }

    //get user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    //update user(face embedding json)
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String userId,
                                                      @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updateFaceEmbeddingJson(userId,file));
    }

}
