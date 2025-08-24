// package com.majlishekhidmat.config;

// import com.majlishekhidmat.service.CustomUserDetailsService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @RequiredArgsConstructor
// public class AdminSecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter; // ✅ Use this one
//     private final CustomUserDetailsService customUserDetailsService;

//     @Bean
//     public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .securityMatcher("/api/admin/**")
//             .cors(Customizer.withDefaults())
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(
//                     HttpMethod.POST,
//                     "/api/auth/admin/login",       // Admin login sabke liye open
//                     "/api/admin/register",          // Admin registration open bina login ke
//                     "/api/admin/forgot-password/**",
//                     "/api/auth/**"
//                 ).permitAll()
//                 .anyRequest().hasAuthority("ROLE_ADMIN")
//             )
//             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .userDetailsService(customUserDetailsService)
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // ✅ Register filter

//         return http.build();
//     }
// }




// package com.majlishekhidmat.config;

// import com.majlishekhidmat.service.CustomUserDetailsService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @RequiredArgsConstructor
// public class AdminSecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter;
//     private final CustomUserDetailsService customUserDetailsService;

//     @Bean
//     public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .securityMatcher("/api/admin/**") // Admin-specific routes
//             .cors(Customizer.withDefaults())
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 // Public admin routes
//                 .requestMatchers(
//                     HttpMethod.POST,
//                     "/api/auth/admin/login",
//                     "/api/admin/register",
//                     "/api/admin/forgot-password/**"
//                 ).permitAll()
//                 // Admin-only routes
//                 .requestMatchers(
//                     "/api/admin/**",
//                     "/api/banks/**",
//                     "/api/volunteers/**",
//                     "/api/v2/campaigns/**",
//                     "/api/v2/inventory/**",
//                     "/api/v2/events/**"
//                 ).hasAuthority("ROLE_ADMIN")
//                 // Shared routes: Admin+User
//                 .requestMatchers("/api/donations/**").hasAnyRole("ADMIN","USER")
//                 .requestMatchers("/api/v2/feedbacks/**").hasAnyRole("ADMIN","USER")
//                 // Any other route needs authentication
//                 .anyRequest().authenticated()
//             )
//             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .userDetailsService(customUserDetailsService)
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }




package com.majlishekhidmat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.majlishekhidmat.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Order(1) // Admin filter priority
public class AdminSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/admin/**")
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public admin routes
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/admin/login",
                    "/api/admin/register",
                    "/api/admin/forgot-password/**"
                ).permitAll()
                // Admin-only routes
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Any other request authenticated
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .userDetailsService(customUserDetailsService)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
