// package com.majlishekhidmat.security;

// import com.majlishekhidmat.entityV2.User;
// import com.majlishekhidmat.repositoryV2.UserRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// @RequiredArgsConstructor
// public class AuthServiceV2

//     private final UserRepositoryV2 userRepository;
//     private final PasswordEncoder passwordEncoder;

//     public User registerUser(User user) {
//         user.setPassword(passwordEncoder.encode(user.getPassword()));
//         return userRepository.save(user);
//     }

//     public User authenticate(String email, String password) {
//         User user = userRepository.findByEmail(email)
//                 .orElseThrow(() -> new RuntimeException("User not found"));
//         if (passwordEncoder.matches(password, user.getPassword())) {
//             return user;
//         } else {
//             throw new RuntimeException("Invalid password");
//         }
//     }
// }
