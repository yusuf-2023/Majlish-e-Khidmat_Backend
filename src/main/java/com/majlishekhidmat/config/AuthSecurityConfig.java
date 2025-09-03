package com.majlishekhidmat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Order(2) // General API filter after admin
public class AuthSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Allow all OPTIONS requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ✅ Public auth routes (Admin + User)
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/login",
                    "/api/auth/admin/login",
                    "/api/auth/refresh-token",
                    "/api/users/register",
                    "/api/users/login",
                    "/api/forgot-password/**"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/api/auth/get-role").permitAll()

                // ✅ The upload-profile-pic endpoint requires authentication
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/users/upload-profile-pic"
                ).authenticated()

                // Role-based routes
                .requestMatchers("/api/stats/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/donations/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/volunteers/**").hasAnyRole("ADMIN","USER")
                .requestMatchers("/api/v2/campaigns/**").hasAnyRole("ADMIN","USER")
                .requestMatchers("/api/inventory/**").hasRole("ADMIN")
                .requestMatchers("/api/v2/feedbacks/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/v2/events/**").hasAnyRole("ADMIN", "USER")
                
                // Activity endpoints
                .requestMatchers("/api/activities/create").hasRole("ADMIN")
                .requestMatchers("/api/activities/update/**").hasRole("ADMIN")
                .requestMatchers("/api/activities/delete/**").hasRole("ADMIN")
                .requestMatchers("/api/activities/all").permitAll()
                .requestMatchers("/api/activities/{id}").permitAll()
                .requestMatchers("/api/activities/like/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/activities/unlike/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/activities/comment/**").hasAnyRole("USER","ADMIN")

                // New route for active banks accessible to USER & ADMIN
                .requestMatchers("/api/banks/active").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/banks/list").hasAnyRole("USER","ADMIN")

                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
