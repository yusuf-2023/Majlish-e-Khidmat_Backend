// package com.majlishekhidmat.config;

// import com.majlishekhidmat.service.CustomUserDetailsService;
// import com.majlishekhidmat.service.JwtService;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.HttpHeaders;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;

// @Component
// @RequiredArgsConstructor
// public class JwtAuthFilter extends OncePerRequestFilter {

//     private final JwtService jwtService;
//     private final CustomUserDetailsService userDetailsService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain chain) throws ServletException, IOException {

//         final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//         String token = null;
//         String email = null;

//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//             token = authHeader.substring(7).trim();
//             try {
//                 email = jwtService.extractUsername(token);
//             } catch (Exception ignored) {
//                 // Invalid token -> let Security handle as unauthorized
//             }
//         }

//         if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//             var userDetails = userDetailsService.loadUserByUsername(email);
//             if (jwtService.validateToken(token, userDetails)) {
//                 var authToken = new UsernamePasswordAuthenticationToken(
//                         userDetails, null, userDetails.getAuthorities()
//                 );
//                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authToken);
//             }
//         }

//         chain.doFilter(request, response);
//     }
// }


package com.majlishekhidmat.config;

import com.majlishekhidmat.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim();
            try {
                email = jwtUtil.extractEmail(token);
            } catch (Exception ignored) {}
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(email);
            if (jwtUtil.validateToken(token)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
