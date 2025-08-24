// package com.majlishekhidmat.security;

// import io.jsonwebtoken.*;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// import java.util.Date;

// @Component
// public class JwtUtils {

//     @Value("${jwt.secret}")
//     private String jwtSecret;

//     @Value("${jwt.accessTokenExpiration}")
//     private long jwtExpirationMs;

//     // Generate JWT token
//     public String generateJwtToken(String email) {
//         return Jwts.builder()
//                 .setSubject(email)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
//                 .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                 .compact();
//     }

//     // Get email from JWT token
//     public String getEmailFromJwtToken(String token) {
//         return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
//     }

//     // Validate JWT token
//     public boolean validateJwtToken(String authToken) {
//         try {
//             Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//             return true;
//         } catch (JwtException | IllegalArgumentException e) {
//             System.err.println("JWT validation error: " + e.getMessage());
//         }
//         return false;
//     }
// }
