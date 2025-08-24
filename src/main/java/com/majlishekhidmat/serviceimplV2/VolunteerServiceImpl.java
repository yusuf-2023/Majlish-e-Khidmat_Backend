package com.majlishekhidmat.serviceimplV2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.majlishekhidmat.dtoV2.VolunteerDto;
import com.majlishekhidmat.entityV2.Volunteer;
import com.majlishekhidmat.repositoryV2.VolunteerRepository;
import com.majlishekhidmat.serviceV2.VolunteerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;

    // Aage badhne se pehle is path ko apne system ke anusaar theek kar lein
    private final String UPLOAD_DIR = "uploads/";

    // ===== Upload profile picture and return filename =====
    // Is method ko public kiya gaya hai taki ise VolunteerController se access kiya ja sake
    public String saveProfilePicture(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        // Make sure upload directory exists
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = Paths.get(UPLOAD_DIR + filename);

        Files.copy(file.getInputStream(), filePath);

        return filename;
    }

    // ===== Delete profile picture file =====
    private void deleteProfilePictureFile(String filename) {
        if (filename != null && !filename.isEmpty()) {
            try {
                Path filePath = Paths.get(UPLOAD_DIR + filename);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Failed to delete old profile picture: " + e.getMessage());
            }
        }
    }

    // ===== Convert DTO to Entity =====
    private Volunteer toEntity(VolunteerDto dto) {
        return Volunteer.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .gender(dto.getGender())
                .dob(dto.getDob())
                .profilePicture(dto.getProfilePicture())
                .occupation(dto.getOccupation())
                .education(dto.getEducation())
                .skills(dto.getSkills())
                .experience(dto.getExperience())
                .availability(dto.getAvailability())
                .joinDate(dto.getJoinDate() != null ? dto.getJoinDate() : LocalDate.now())
                .verified(dto.getVerified() != null ? dto.getVerified() : false)
                .build();
    }

    // ===== Convert Entity to DTO =====
    private VolunteerDto toDto(Volunteer entity) {
        VolunteerDto dto = new VolunteerDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setGender(entity.getGender());
        dto.setDob(entity.getDob());
        dto.setProfilePicture(entity.getProfilePicture());
        dto.setOccupation(entity.getOccupation());
        dto.setEducation(entity.getEducation());
        dto.setSkills(entity.getSkills());
        dto.setExperience(entity.getExperience());
        dto.setAvailability(entity.getAvailability());
        dto.setJoinDate(entity.getJoinDate());
        dto.setVerified(entity.getVerified());
        return dto;
    }

    // ===== CREATE =====
    @Override
    public VolunteerDto createVolunteer(VolunteerDto dto) {
        Volunteer volunteer = toEntity(dto);
        Volunteer saved = volunteerRepository.save(volunteer);
        return toDto(saved);
    }

    // ===== DELETE (with file cleanup) =====
    @Override
    public void deleteVolunteer(Long id) {
        Volunteer existing = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found with id: " + id));

        deleteProfilePictureFile(existing.getProfilePicture());

        volunteerRepository.delete(existing);
    }

    // ===== GET BY ID =====
    @Override
    public VolunteerDto getVolunteerById(Long id) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found with id: " + id));
        return toDto(volunteer);
    }

    // ===== GET ALL =====
    @Override
    public List<VolunteerDto> getAllVolunteers() {
        return volunteerRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // ===== 1. INTERFACE ko satisfy karne wala method =====
    @Override
    public VolunteerDto updateVolunteer(Long id, VolunteerDto dto) {
        return this.updateVolunteer(id, dto, null);
    }

    // ===== 2. ASLI LOGIC wala private method (overloading) =====
    public VolunteerDto updateVolunteer(Long id, VolunteerDto dto, MultipartFile profilePicture) {
        Volunteer existing = volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found with id: " + id));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setAddress(dto.getAddress());
        existing.setGender(dto.getGender());
        existing.setDob(dto.getDob());
        existing.setSkills(dto.getSkills());
        existing.setOccupation(dto.getOccupation());
        existing.setEducation(dto.getEducation());
        existing.setExperience(dto.getExperience());
        existing.setAvailability(dto.getAvailability());

        if (profilePicture != null && !profilePicture.isEmpty()) {
            deleteProfilePictureFile(existing.getProfilePicture());
            try {
                String filename = saveProfilePicture(profilePicture);
                existing.setProfilePicture(filename);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save profile picture", e);
            }
        }
        else if (dto.getProfilePicture() == null || (dto.getProfilePicture() != null && dto.getProfilePicture().isEmpty())) {
            deleteProfilePictureFile(existing.getProfilePicture());
            existing.setProfilePicture(null);
        }

        Volunteer updated = volunteerRepository.save(existing);
        return toDto(updated);
    }
}