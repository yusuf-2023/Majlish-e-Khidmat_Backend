// package com.majlishekhidmat.security;

// import com.majlishekhidmat.entity.User;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.Map;

// @RestController

// @RequestMapping("/api/security")
// @RequiredArgsConstructor
// @CrossOrigin(origins = "*")
// public class SecurityAuthController {

//     private final AuthServiceV2 authService;
//     private final JwtUtils jwtUtils;

//     // User Registration
//     @PostMapping("/register")
//     public ResponseEntity<User> register(@RequestBody User user) {
//         User savedUser = authService.registerUser(user);
//         return ResponseEntity.ok(savedUser);
//     }

//     // User Login
//     @PostMapping("/login")
//     public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
//         User authenticatedUser = authService.authenticate(user.getEmail(), user.getPassword());
//         String token = jwtUtils.generateJwtToken(authenticatedUser.getEmail());
//         return ResponseEntity.ok(Map.of(
//                 "token", token,
//                 "email", authenticatedUser.getEmail(),
//                 "role", authenticatedUser.getRole()
//         ));
//     }
// }
