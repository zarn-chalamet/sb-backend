package com.trainee_project.attendance_tracker_springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "office")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfficeLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double latitude;

    private double longitude;

    private int radiusMeters = 200;

}
