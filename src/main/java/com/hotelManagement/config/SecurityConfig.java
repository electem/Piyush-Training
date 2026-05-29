package com.hotelManagement.config;

import com.hotelManagement.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // allow preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // PUBLIC APIs
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/uploads/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // read access for authenticated USER and ADMIN
                        .requestMatchers(HttpMethod.GET,
                                "/api/hotels/**",
                                "/api/rooms/**",
                                "/hotel-images/**",
                                "/hotels/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // write access for ADMIN only
                        .requestMatchers(HttpMethod.POST,
                                "/api/hotels/**",
                                "/api/rooms/**",
                                "/hotel-images/**",
                                "/hotels/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/hotels/**",
                                "/api/rooms/**",
                                "/hotel-images/**",
                                "/hotels/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/hotels/**",
                                "/api/rooms/**",
                                "/hotel-images/**",
                                "/hotels/**"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
