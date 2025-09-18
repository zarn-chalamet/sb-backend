package com.trainee_project.attendance_tracker_springboot.repository;

import com.trainee_project.attendance_tracker_springboot.model.OfficeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeLocationRepository extends JpaRepository<OfficeLocation, Long> {
    boolean existsByName(String name);
}
