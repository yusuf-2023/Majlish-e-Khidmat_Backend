// package com.majlishekhidmat.config;

// import io.jsonwebtoken.*;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import jakarta.annotation.PostConstruct;

// import java.security.Key;
// import java.util.Base64;
// import java.util.Date;

// @Component
// public class JwtUtil {

//     @Value("${jwt.secret}")
//     private String secret;  // Base64 encoded secret from properties

//     private Key key;

//     // Token expiration time - yeh tum properties me bhi define kar sakte ho agar chaho
//     private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

//     @PostConstruct
//     public void init() {
//         // Secret ko decode karo aur key generate karo
//         byte[] decodedKey = Base64.getDecoder().decode(secret);
//         this.key = Keys.hmacShaKeyFor(decodedKey);
//     }

//     // Token generate karo with email and role
//     public String generateToken(String email, String role) {
//         return Jwts.builder()
//                 .setSubject(email)
//                 .claim("roles", role)          // Role claim
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                 .signWith(key)
//                 .compact();
//     }

//     // Email token se nikalo
//     public String extractEmail(String token) {
//         return getClaims(token).getSubject();
//     }

//     // Role token se nikalo
//     public String extractRole(String token) {
//         return (String) getClaims(token).get("roles");
//     }

//     // Token validate karo
//     public boolean validateToken(String token) {
//         try {
//             getClaims(token);
//             return true;
//         } catch (JwtException | IllegalArgumentException e) {
//             return false;
//         }
//     }

//     private Claims getClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(key)
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }
// }






package com.majlishekhidmat.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) getClaims(token).get("roles");
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

