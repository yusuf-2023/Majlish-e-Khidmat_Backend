// package com.majlishekhidmat.config;

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
// public class AuthSecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter; // ✅ Replace here

//     @Bean
//     public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .securityMatcher("/api/auth/**")
//             .cors(Customizer.withDefaults())
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(
//                     HttpMethod.POST,
//                     "/api/auth/login",
//                     "/api/auth/admin/login",
//                     "/api/auth/refresh-token"
//                 ).permitAll()
//                 .requestMatchers(
//                     HttpMethod.GET,
//                     "/api/auth/get-role"
//                 ).permitAll()
//                 .anyRequest().authenticated()
//             )
//             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // ✅ Register filter

//         return http.build();
//     }
// }







// package com.majlishekhidmat.config;

// import lombok.RequiredArgsConstructor;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @RequiredArgsConstructor
// @EnableMethodSecurity(prePostEnabled = true)
// public class AuthSecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .securityMatcher("/api/**") // General API routes
//             .cors(Customizer.withDefaults())
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 // Public auth routes
//                 .requestMatchers(
//                     HttpMethod.POST,
//                     "/api/auth/login",
//                     "/api/auth/admin/login",
//                     "/api/auth/refresh-token",
//                     "/api/users/register",        // Add user public register here too
//                     "/api/users/login",           // Public user login
//                     "/api/users/forgot-password/**"
//                 ).permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/auth/get-role").permitAll()

//                 // Role-based routes
//                 .requestMatchers("/api/stats/**").hasRole("ADMIN")
//                 .requestMatchers("/api/donations/**").hasAnyRole("ADMIN", "USER")
//                 .requestMatchers("/api/volunteers/**").hasRole("ADMIN")
//                 .requestMatchers("/api/v2/campaigns/**").hasRole("ADMIN")
//                 .requestMatchers("/api/v2/inventory/**").hasRole("ADMIN")
//                 .requestMatchers("/api/v2/feedbacks/**").hasAnyRole("ADMIN", "USER")
//                 .requestMatchers("/api/v2/events/**").hasRole("ADMIN")

//                 // Default: authenticated
//                 .anyRequest().authenticated()
//             )
//             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }






// package com.majlishekhidmat.config;

// import lombok.RequiredArgsConstructor;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @RequiredArgsConstructor
// @EnableMethodSecurity(prePostEnabled = true)
// public class AuthSecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .securityMatcher("/api/**") // General API routes
//             .cors(Customizer.withDefaults())
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 // Public auth routes
//                 .requestMatchers(
//                     HttpMethod.POST,
//                     "/api/auth/login",
//                     "/api/auth/admin/login",
//                     "/api/auth/refresh-token",
//                     "/api/users/register",
//                     "/api/users/login",
//                     "/api/users/forgot-password/**"
//                 ).permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/auth/get-role").permitAll()
//                 // Role-based routes
//                 .requestMatchers("/api/stats/**").hasRole("ADMIN")
//                 .requestMatchers("/api/donations/**").hasAnyRole("ADMIN", "USER")
//                 .requestMatchers("/api/volunteers/**").hasRole("ADMIN")
//                 .requestMatchers("/api/v2/campaigns/**").hasRole("ADMIN")
//                 .requestMatchers("/api/v2/inventory/**").hasRole("ADMIN")
//                 .requestMatchers("/api/v2/feedbacks/**").hasAnyRole("ADMIN", "USER")
//                 .requestMatchers("/api/v2/events/**").hasRole("ADMIN")
//                 // Default: authenticated
//                 .anyRequest().authenticated()
//             )
//             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                    "/api/auth/admin/login",   // ✅ Fix: admin login open
                    "/api/auth/refresh-token",
                    "/api/users/register",
                    "/api/users/login",
                    "/api/users/forgot-password/**"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/api/auth/get-role").permitAll()

                // Role-based routes
                .requestMatchers("/api/stats/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/donations/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/volunteers/**").hasAnyRole("ADMIN","USER")
                .requestMatchers("/api/v2/campaigns/**").hasAnyRole("ADMIN","USER")
                .requestMatchers("/api/v2/inventory/**").hasRole("ADMIN")
                .requestMatchers("/api/v2/feedbacks/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/v2/events/**").hasAnyRole("ADMIN", "USER")

                // ✅ New route for active banks accessible to USER & ADMIN
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

