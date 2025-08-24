// package com.majlishekhidmat.serviceimpl;

// import com.majlishekhidmat.dto.LoginDto;
// import com.majlishekhidmat.entity.Admin;
// import com.majlishekhidmat.entity.User;
// import com.majlishekhidmat.repository.AdminRepository;
// import com.majlishekhidmat.repository.UserRepository;
// import com.majlishekhidmat.service.AuthService;
// import com.majlishekhidmat.config.JwtUtil;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import java.util.Optional;

// @Service
// @RequiredArgsConstructor
// public class AuthServiceImpl implements AuthService {

//     private final UserRepository userRepository;
//     private final AdminRepository adminRepository;
//     private final PasswordEncoder passwordEncoder;
//     private final JwtUtil jwtUtil;

//     private String fixRole(String role, String defaultRole) {
//         if (role == null || role.isEmpty()) return defaultRole;
//         if (!role.startsWith("ROLE_")) {
//             return "ROLE_" + role.toUpperCase();
//         }
//         return role;
//     }

//     @Override
//     public String login(LoginDto loginDto) {
//         String email = loginDto.getEmail().trim();            // Trim email
//         String rawPassword = loginDto.getPassword().trim();   // Trim password

//         System.out.println("Trying login for: '" + email + "'");
//         System.out.println("Raw password: '" + rawPassword + "'");

//         Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
//         if (userOpt.isPresent()) {
//             User user = userOpt.get();
//             System.out.println("User found: " + user.getEmail());
//             System.out.println("Encoded password in DB: " + user.getPassword());

//             boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
//             System.out.println("Password matches: " + matches);

//             if (matches) {
//                 String role = fixRole(user.getRole(), "ROLE_USER");
//                 System.out.println("User role from DB / token: " + role);
//                 String token = jwtUtil.generateToken(user.getEmail(), role);
//                 System.out.println("JWT token generated for user");
//                 return token;
//             } else {
//                 throw new RuntimeException("Invalid password for user");
//             }
//         } else {
//             System.out.println("User not found for email: " + email);
//         }

//         Optional<Admin> adminOpt = adminRepository.findByEmailIgnoreCase(email);
//         if (adminOpt.isPresent()) {
//             Admin admin = adminOpt.get();
//             System.out.println("Admin found with email: " + admin.getEmail());

//             boolean matches = passwordEncoder.matches(rawPassword, admin.getPassword());
//             System.out.println("Password match result: " + matches);

//             if (matches) {
//                 String role = fixRole(admin.getRole(), "ROLE_ADMIN");
//                 System.out.println("Admin role from DB / token: " + role);
//                 String token = jwtUtil.generateToken(admin.getEmail(), role);
//                 System.out.println("JWT token generated for admin");
//                 return token;
//             } else {
//                 throw new RuntimeException("Invalid password for admin");
//             }
//         }

//         throw new RuntimeException("User or Admin not found");
//     }
// }





package com.majlishekhidmat.serviceimpl;

import com.majlishekhidmat.dto.LoginDto;
import com.majlishekhidmat.entity.Admin;
import com.majlishekhidmat.entity.User;
import com.majlishekhidmat.repository.AdminRepository;
import com.majlishekhidmat.repository.UserRepository;
import com.majlishekhidmat.service.AuthService;
import com.majlishekhidmat.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private String fixRole(String role, String defaultRole) {
        if (role == null || role.isEmpty()) return defaultRole;
        if (!role.startsWith("ROLE_")) return "ROLE_" + role.toUpperCase();
        return role;
    }

    @Override
    public String login(LoginDto loginDto) {
        String email = loginDto.getEmail().trim();
        String rawPassword = loginDto.getPassword().trim();

        // --- Check User first ---
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                String role = fixRole(user.getRole(), "ROLE_USER");
                return jwtUtil.generateToken(user.getEmail(), role);
            } else {
                throw new RuntimeException("Invalid password for user");
            }
        }

        // --- Check Admin if User not found ---
        Optional<Admin> adminOpt = adminRepository.findByEmailIgnoreCase(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (passwordEncoder.matches(rawPassword, admin.getPassword())) {
                String role = fixRole(admin.getRole(), "ROLE_ADMIN");
                return jwtUtil.generateToken(admin.getEmail(), role);
            } else {
                throw new RuntimeException("Invalid password for admin");
            }
        }

        throw new RuntimeException("User or Admin not found");
    }
}
