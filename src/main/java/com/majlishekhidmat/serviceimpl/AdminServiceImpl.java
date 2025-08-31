package com.majlishekhidmat.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.majlishekhidmat.dto.AdminDto;
import com.majlishekhidmat.entity.Admin;
import com.majlishekhidmat.repository.AdminRepository;
import com.majlishekhidmat.service.AdminService;
import com.majlishekhidmat.service.ProfilePicUploadService; // ✅ Naya dependency

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfilePicUploadService profilePicUploadService; // ✅ Naya dependency

    @Override
    public Admin registerAdmin(AdminDto adminDto, MultipartFile file) {
        if (adminRepository.findByEmailIgnoreCase(adminDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        Admin admin = Admin.builder()
                .name(adminDto.getName())
                .email(adminDto.getEmail())
                .password(passwordEncoder.encode(adminDto.getPassword()))
                .role("ROLE_ADMIN")
                .phone(adminDto.getPhone())
                .address(adminDto.getAddress())
                .dob(adminDto.getDob())
                .gender(adminDto.getGender())
                .profilePic(adminDto.getProfilePic())
                .build();
        
        // ✅ File upload logic
        if (file != null && !file.isEmpty()) {
            try {
                String filePath = profilePicUploadService.saveProfilePic(file);
                admin.setProfilePic(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile picture", e);
            }
        }

        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public String deleteAdmin(Long id) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            adminRepository.deleteById(id);
            return "Admin deleted successfully";
        } else {
            return "Admin not found with id: " + id;
        }
    }

    @Override
    public Admin updateAdmin(Long id, AdminDto adminDto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

        admin.setName(adminDto.getName());
        admin.setEmail(adminDto.getEmail());
        admin.setPhone(adminDto.getPhone());
        admin.setAddress(adminDto.getAddress());
        admin.setDob(adminDto.getDob());
        admin.setGender(adminDto.getGender());

        if (adminDto.getProfilePic() != null) {
            admin.setProfilePic(adminDto.getProfilePic());
        }

        if (adminDto.getPassword() != null && !adminDto.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        }

        if (admin.getRole() == null || !admin.getRole().startsWith("ROLE_")) {
            admin.setRole("ROLE_ADMIN");
        }

        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));
    }

    @Override
    public Admin updateProfilePic(String email, MultipartFile file) {
        Admin admin = getAdminByEmail(email);
        try {
            String filePath = profilePicUploadService.saveProfilePic(file);
            admin.setProfilePic(filePath);
            return adminRepository.save(admin);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }
}
