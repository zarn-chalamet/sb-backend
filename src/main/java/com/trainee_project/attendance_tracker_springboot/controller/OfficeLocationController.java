package com.trainee_project.attendance_tracker_springboot.controller;

import com.trainee_project.attendance_tracker_springboot.dto.OfficeLocationRequestDto;
import com.trainee_project.attendance_tracker_springboot.dto.OfficeLocationResponseDto;
import com.trainee_project.attendance_tracker_springboot.dto.UserResponseDto;
import com.trainee_project.attendance_tracker_springboot.service.OfficeLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/offices")
@RequiredArgsConstructor
public class OfficeLocationController {

    private final OfficeLocationService officeLocationService;

    // only admin can
    //create office
    @PostMapping("/create")
    public ResponseEntity<OfficeLocationResponseDto> createNewOffice(@RequestBody OfficeLocationRequestDto request) {

        OfficeLocationResponseDto office = officeLocationService.createOffice(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(office);
    }

    //get office list
    @GetMapping()
    public ResponseEntity<List<OfficeLocationResponseDto>> getOffices() {

        List<OfficeLocationResponseDto> offices = officeLocationService.getOfficeLists();

        return ResponseEntity.ok(offices);
    }

    //get user list of office
    @GetMapping("/{officeId}/users")
    public ResponseEntity<List<UserResponseDto>> getUserListByOffice(@PathVariable String officeId) {

        List<UserResponseDto> userList = officeLocationService.getUserListByOffice(officeId);

        return ResponseEntity.ok(userList);
    }

    //get office by id
    @GetMapping("/{officeId}")
    public ResponseEntity<OfficeLocationResponseDto> getOffice(@PathVariable String officeId) {

        OfficeLocationResponseDto office = officeLocationService.getOfficeById(officeId);

        return ResponseEntity.ok(office);
    }

    //update office
    @PutMapping("/{officeId}")
    public ResponseEntity<OfficeLocationResponseDto> updateOffice(@PathVariable String officeId,
                                                                  @RequestBody OfficeLocationRequestDto request) {

        OfficeLocationResponseDto office = officeLocationService.updateOfficeById(officeId,request);

        return ResponseEntity.ok(office);
    }

    //delete office
    @DeleteMapping("/{officeId}")
    public ResponseEntity<?> deleteOffice(@PathVariable String officeId) {

        officeLocationService.deleteOfficeById(officeId);

        return ResponseEntity.ok("Deleted office successfully");
    }
}
