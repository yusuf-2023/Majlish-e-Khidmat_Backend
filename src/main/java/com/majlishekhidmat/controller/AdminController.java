package com.majlishekhidmat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart; // ✅ Yeh import zaroori hai
import org.springframework.http.MediaType; // ✅ Yeh import zaroori hai

import com.fasterxml.jackson.databind.ObjectMapper;
import com.majlishekhidmat.dto.AdminDto;
import com.majlishekhidmat.entity.Admin;
import com.majlishekhidmat.service.AdminService;
import com.majlishekhidmat.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173" })
public class AdminController {

    private final AdminService adminService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/all")
    public ResponseEntity<?> getAllAdmins() {
        try {
            List<Admin> admins = adminService.getAllAdmins();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", admins,
                    "message", "Admins fetched successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Failed to fetch admins: " + e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getAdminProfile(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            String email = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            if (!"ROLE_ADMIN".equalsIgnoreCase(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "error", "Access denied: Role is not ADMIN"));
            }

            Admin admin = adminService.getAdminByEmail(email);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", createAdminResponse(admin),
                    "message", "Admin profile fetched successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "error", "Invalid or expired token: " + e.getMessage()));
        }
    }

    // ✅ Naya `registerAdmin` method jo `multipart/form-data` ko handle karta hai
    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> registerAdmin(
            @RequestPart("data") AdminDto adminDto, // ✅ 'adminData' ko 'data' se badal diya gaya hai
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // Ab file ko bhi service method ko pass karein
            Admin registeredAdmin = adminService.registerAdmin(adminDto, file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", createAdminResponse(registeredAdmin),
                    "message", "Admin registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "error", "Registration failed: " + e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAdminProfile(HttpServletRequest request, @RequestBody AdminDto adminDto) {
        try {
            String token = extractToken(request);
            String email = jwtService.extractUsername(token);
            Admin existing = adminService.getAdminByEmail(email);

            if (existing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "Admin not found"));
            }

            adminDto.setId(existing.getId());

            Admin updated = adminService.updateAdmin(existing.getId(), adminDto);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", createAdminResponse(updated),
                    "message", "Admin updated successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Update failed: " + e.getMessage()));
        }
    }

    @PostMapping("/upload-profile-pic")
    public ResponseEntity<?> uploadAdminProfilePic(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        try {
            String token = extractToken(request);
            String email = jwtService.extractUsername(token);

            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "error", "No file selected"));
            }

            Admin updated = adminService.updateProfilePic(email, file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", createAdminResponse(updated),
                    "message", "Profile picture updated successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Failed to upload profile picture: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAdminByToken(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            String email = jwtService.extractUsername(token);
            Admin admin = adminService.getAdminByEmail(email);

            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "Admin not found"));
            }

            String message = adminService.deleteAdmin(admin.getId());
            return ResponseEntity.ok(Map.of("success", true, "message", message));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "error", "Deletion failed: " + e.getMessage()));
        }
    }

    private Map<String, Object> createAdminResponse(Admin admin) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", admin.getId());
        response.put("name", admin.getName());
        response.put("email", admin.getEmail());
        response.put("role", "ROLE_ADMIN");
        response.put("phone", admin.getPhone());
        response.put("address", admin.getAddress());
        response.put("dob", admin.getDob());
        response.put("gender", admin.getGender());
        response.put("profilePic", admin.getProfilePic());
        return response;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }
}
