package com.majlishekhidmat.entityV2;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private LocalDate dob;

    // Profile Info
    private String profilePicture;   // Volunteer profile image (URL or file path)
    private String occupation;       // Student / Employee / Business / Other
    private String education;        // Qualification
    private String skills;           // Skills (teaching, medical, technical, etc.)
    private String experience;       // Previous volunteering experience
    private String availability;     // Full-time / Part-time / Weekends
    
    // Other Info
    private LocalDate joinDate;      // When they registered
    private Boolean verified;        // Admin verified or not
}
