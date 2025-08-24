// package com.majlishekhidmat.service;

// import com.majlishekhidmat.entity.Admin;
// import com.majlishekhidmat.entity.User;
// import com.majlishekhidmat.repository.AdminRepository;
// import com.majlishekhidmat.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.*;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// @RequiredArgsConstructor
// public class CustomUserDetailsService implements UserDetailsService {

//     private final UserRepository userRepository;
//     private final AdminRepository adminRepository;

//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//         var user = userRepository.findByEmailIgnoreCase(email).orElse(null);
//         if (user != null) {
//             String role = normalizeRole(user.getRole(), "USER");
//             return org.springframework.security.core.userdetails.User
//                     .withUsername(user.getEmail())
//                     .password(user.getPassword())
//                     .authorities(List.of(new SimpleGrantedAuthority(role)))
//                     .build();
//         }

//         var admin = adminRepository.findByEmailIgnoreCase(email)
//                 .orElseThrow(() -> new UsernameNotFoundException("User/Admin not found: " + email));

//         String role = normalizeRole(admin.getRole(), "ADMIN");
//         return org.springframework.security.core.userdetails.User
//                 .withUsername(admin.getEmail())
//                 .password(admin.getPassword())
//                 .authorities(List.of(new SimpleGrantedAuthority(role)))
//                 .build();
//     }

//     private String normalizeRole(String role, String def) {
//         if (role == null || role.isBlank()) return "ROLE_" + def.toUpperCase();
//         return role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
//         }
// }





package com.majlishekhidmat.service;

import com.majlishekhidmat.entity.Admin;
import com.majlishekhidmat.entity.User;
import com.majlishekhidmat.repository.AdminRepository;
import com.majlishekhidmat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // --- Check User first ---
        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if (user != null) {
            String role = normalizeRole(user.getRole(), "USER");
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .build();
        }

        // --- If User not found, check Admin ---
        Admin admin = adminRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User/Admin not found with email: " + email));

        String role = normalizeRole(admin.getRole(), "ADMIN");
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password(admin.getPassword())
                .authorities(authorities)
                .build();
    }

    private String normalizeRole(String role, String defaultRole) {
        if (role == null || role.isEmpty()) return "ROLE_" + defaultRole.toUpperCase();
        if (!role.startsWith("ROLE_")) return "ROLE_" + role.toUpperCase();
        return role;
    }
}
