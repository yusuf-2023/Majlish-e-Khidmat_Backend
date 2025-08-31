package com.majlishekhidmat.controller;

import com.majlishekhidmat.dto.LoginRequest;
import com.majlishekhidmat.dto.UserDto;
import com.majlishekhidmat.entity.User;
import com.majlishekhidmat.service.UserService;
import com.majlishekhidmat.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173" })
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // -------- Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto dto) {
        User saved = userService.registerUser(dto);
        return ResponseEntity.ok(saved);
    }

    // -------- Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userService.getUserByEmail(req.getEmail());
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        Map<String, Object> resp = new HashMap<>();
        resp.put("accessToken", token);
        resp.put("role", "ROLE_" + user.getRole().toUpperCase());
        resp.put("email", user.getEmail());
        resp.put("name", user.getName());

        return ResponseEntity.ok(resp);
    }

    // -------- Get all users (Admin only)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // -------- Get profile (Logged-in user)
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            String email = jwtService.extractUsername(token);

            if (email == null || email.isEmpty())
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid token or email missing"));

            User user = userService.getUserByEmail(email);
            if (user == null)
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));

            Map<String, Object> resp = new HashMap<>();
            resp.put("email", user.getEmail());
            resp.put("name", user.getName());
            resp.put("role", "ROLE_" + user.getRole().toUpperCase());
            resp.put("phone", user.getPhone());
            resp.put("address", user.getAddress());
            resp.put("dob", user.getDob());
            resp.put("gender", user.getGender());
            resp.put("profilePic", user.getProfilePic());

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to fetch profile: " + e.getMessage()));
        }
    }

    // -------- Update profile (Logged-in user)
    @PutMapping("/update")
    public ResponseEntity<?> update(HttpServletRequest request, @RequestBody UserDto dto) {
        try {
            String token = extractToken(request);
            String email = jwtService.extractUsername(token);
            User updated = userService.updateUser(email, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    // -------- Self Delete profile (Logged-in user)
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            String email = jwtService.extractUsername(token);
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
            }
            userService.deleteUserById(user.getId());
            return ResponseEntity.ok(Map.of("message", "Your account has been deleted successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    // -------- Admin Delete User by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found with id " + id));
            }
            userService.deleteUserById(id);
            return ResponseEntity.ok(Map.of("message", "User with id " + id + " has been deleted successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    // -------- Upload profile picture
    @PostMapping("/upload-profile-pic")
    public ResponseEntity<?> uploadProfile(HttpServletRequest request, @RequestParam MultipartFile file) {
        try {
            String token = extractToken(request);
            String email = jwtService.extractUsername(token);
            User updated = userService.uploadProfilePic(email, file);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }

    // -------- Helper to extract token from Authorization header
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }
}
