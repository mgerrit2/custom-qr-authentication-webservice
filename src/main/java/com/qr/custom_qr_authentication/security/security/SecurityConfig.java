package com.qr.custom_qr_authentication.security.security;

import com.qr.custom_qr_authentication.config.twoway.CustomJwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@SuppressWarnings("java:S4502")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomJwtAuthFilter customJwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable) // CSRF protections should not be disabled is save
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to Swagger UI and OpenAPI documentation
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // All other endpoints (like /api/protected) require authentication
                        .anyRequest().authenticated()
                )
                // Register custom JWT filter instead of oauth2ResourceServer
                .addFilterBefore(customJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
