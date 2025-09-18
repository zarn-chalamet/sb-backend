package com.trainee_project.attendance_tracker_springboot.repository;

import com.trainee_project.attendance_tracker_springboot.model.OfficeLocation;
import com.trainee_project.attendance_tracker_springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByAssignedOffice_Id(Long officeId);

    List<User> findByAssignedOffice(OfficeLocation office);
}
