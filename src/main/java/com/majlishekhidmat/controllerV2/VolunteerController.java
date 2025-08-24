package com.majlishekhidmat.controllerV2;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.majlishekhidmat.dtoV2.VolunteerDto;
import com.majlishekhidmat.serviceimplV2.VolunteerServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/volunteers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VolunteerController {

    private final VolunteerServiceImpl volunteerService;

    // ==================== PUBLIC REGISTER ====================
    @PostMapping("/register")
    public ResponseEntity<VolunteerDto> registerVolunteer(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam(value="address", required=false) String address,
            @RequestParam(value="gender", required=false) String gender,
            @RequestParam(value="dob", required=false) String dob,
            @RequestParam(value="skills", required=false) String skills,
            @RequestParam(value="occupation", required=false) String occupation,
            @RequestParam(value="education", required=false) String education,
            @RequestParam(value="experience", required=false) String experience,
            @RequestParam(value="availability", required=false) String availability,
            @RequestParam(value="profilePicture", required=false) MultipartFile profilePicture
    ) throws IOException {

        VolunteerDto dto = new VolunteerDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPhone(phone);
        dto.setAddress(address);
        dto.setGender(gender);
        if(dob != null && !dob.isEmpty()) dto.setDob(LocalDate.parse(dob));
        dto.setSkills(skills);
        dto.setOccupation(occupation);
        dto.setEducation(education);
        dto.setExperience(experience);
        dto.setAvailability(availability);

        if(profilePicture != null && !profilePicture.isEmpty()) {
            String filename = volunteerService.saveProfilePicture(profilePicture);
            dto.setProfilePicture(filename);
        }

        return ResponseEntity.ok(volunteerService.createVolunteer(dto));
    }

    // ==================== ADMIN CRUD ====================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VolunteerDto> createVolunteer(@RequestParam("name") String name,
                                                         @RequestParam("email") String email,
                                                         @RequestParam("phone") String phone) {
        VolunteerDto dto = new VolunteerDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPhone(phone);
        return ResponseEntity.ok(volunteerService.createVolunteer(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<VolunteerDto> getVolunteer(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerService.getVolunteerById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<VolunteerDto>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }

    // ✅ Corrected PUT endpoint: @RequestParam for FormData fields
    @PutMapping(path = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VolunteerDto> updateVolunteer(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value="phone", required=false) String phone,
            @RequestParam(value="address", required=false) String address,
            @RequestParam(value="gender", required=false) String gender,
            @RequestParam(value="dob", required=false) String dob,
            @RequestParam(value="skills", required=false) String skills,
            @RequestParam(value="occupation", required=false) String occupation,
            @RequestParam(value="education", required=false) String education,
            @RequestParam(value="experience", required=false) String experience,
            @RequestParam(value="availability", required=false) String availability,
            @RequestParam(value="profilePicture", required=false) MultipartFile profilePicture
    ) throws IOException {

        VolunteerDto dto = new VolunteerDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPhone(phone);
        dto.setAddress(address);
        dto.setGender(gender);
        if(dob != null && !dob.isEmpty()) dto.setDob(LocalDate.parse(dob));
        dto.setSkills(skills);
        dto.setOccupation(occupation);
        dto.setEducation(education);
        dto.setExperience(experience);
        dto.setAvailability(availability);

        VolunteerDto updatedVolunteer = volunteerService.updateVolunteer(id, dto, profilePicture);
        return ResponseEntity.ok(updatedVolunteer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVolunteer(@PathVariable Long id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.ok("Volunteer deleted successfully");
    }
}
