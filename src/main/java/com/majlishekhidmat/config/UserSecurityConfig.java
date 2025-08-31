// // package com.majlishekhidmat.config;

// // import com.majlishekhidmat.service.CustomUserDetailsService;
// // import lombok.RequiredArgsConstructor;
// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.http.HttpMethod;
// // import org.springframework.security.config.Customizer;
// // import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// // import org.springframework.security.config.http.SessionCreationPolicy;
// // import org.springframework.security.web.SecurityFilterChain;
// // import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// // @Configuration
// // @RequiredArgsConstructor
// // public class UserSecurityConfig {

// //     private final JwtAuthFilter jwtAuthFilter;
// //     private final CustomUserDetailsService customUserDetailsService;

// //     @Bean
// //     public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
// //         http
// //             .securityMatcher("/api/users/**") // User-specific routes
// //             .cors(Customizer.withDefaults())
// //             .csrf(csrf -> csrf.disable())
// //             .authorizeHttpRequests(auth -> auth
// //                 .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
// //                 // Public routes
// //                 .requestMatchers(
// //                     HttpMethod.POST,
// //                     "/api/users/register",
// //                     "/api/users/login",
// //                     "/api/users/forgot-password/**"
// //                 ).permitAll()
// //                 // All other routes need JWT
// //                 .anyRequest().authenticated()
// //             )
// //             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// //             .userDetailsService(customUserDetailsService)
// //             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

// //         return http.build();
// //     }
// // }








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
// public class UserSecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter;
//     private final CustomUserDetailsService customUserDetailsService;

//     @Bean
//     public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .securityMatcher("/api/users/**") // User-specific routes
//             .cors(Customizer.withDefaults())
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                 // Public routes
//                 .requestMatchers(
//                     HttpMethod.POST,
//                     "/api/users/register",
//                     "/api/users/login",
//                     "/api/users/forgot-password/**"
//                 ).permitAll()
//                 // All other routes need JWT
//                 .anyRequest().authenticated()
//             )
//             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .userDetailsService(customUserDetailsService)
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }
