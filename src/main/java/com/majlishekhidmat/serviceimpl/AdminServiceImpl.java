package com.majlishekhidmat.serviceimpl;

import com.majlishekhidmat.dto.AdminDto;
import com.majlishekhidmat.entity.Admin;
import com.majlishekhidmat.repository.AdminRepository;
import com.majlishekhidmat.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Admin registerAdmin(AdminDto adminDto) {
        // Check if email already exists
        if (adminRepository.findByEmailIgnoreCase(adminDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        Admin admin = Admin.builder()
                .name(adminDto.getName())
                .email(adminDto.getEmail())
                .password(passwordEncoder.encode(adminDto.getPassword()))
                .role("ROLE_ADMIN")    // ROLE_ prefix mandatory here
                .phone(adminDto.getPhone())
                .address(adminDto.getAddress())
                .dob(adminDto.getDob())
                .gender(adminDto.getGender())
                .profilePic(adminDto.getProfilePic())
                .build();

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

        // Ensure role is "ROLE_ADMIN"
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

        String uploadDir = "uploads/admin/";
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File uploadPath = new File(uploadDir);

        if (!uploadPath.exists()) {
            boolean created = uploadPath.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory for uploads");
            }
        }

        try {
            File destinationFile = new File(uploadPath, fileName);
            file.transferTo(destinationFile);
            admin.setProfilePic(uploadDir + fileName);
            return adminRepository.save(admin);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }
}
