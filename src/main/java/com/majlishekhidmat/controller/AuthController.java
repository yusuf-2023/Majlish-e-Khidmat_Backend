








package com.majlishekhidmat.controller;

import com.majlishekhidmat.dto.LoginDto;
import com.majlishekhidmat.entity.Admin;
import com.majlishekhidmat.entity.User;
import com.majlishekhidmat.repository.AdminRepository;
import com.majlishekhidmat.repository.UserRepository;
import com.majlishekhidmat.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @GetMapping("/get-role")
    public ResponseEntity<?> getRole(@RequestParam String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(user -> ResponseEntity.ok(Map.of("role", user.getRole())))
                .or(() -> adminRepository.findByEmailIgnoreCase(email)
                        .map(admin -> ResponseEntity.ok(Map.of("role", admin.getRole())))
                )
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return handleLogin(loginDto, false);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginDto loginDto) {
        return handleLogin(loginDto, true);
    }

    private ResponseEntity<?> handleLogin(LoginDto loginDto, boolean isAdmin) {
        try {
            String token = authService.login(loginDto);

            String email = loginDto.getEmail();
            String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", token);
            response.put("role", role);

            if (isAdmin) {
                Admin admin = adminRepository.findByEmailIgnoreCase(email)
                        .orElseThrow(() -> new RuntimeException("Admin not found"));
                response.put("email", admin.getEmail());
                response.put("name", admin.getName());
            } else {
                User user = userRepository.findByEmailIgnoreCase(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                response.put("email", user.getEmail());
                response.put("name", user.getName());
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
