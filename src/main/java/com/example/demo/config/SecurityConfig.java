package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/signup", "/api/users/login").permitAll() // Allow public access to signup/login
                .requestMatchers("/api/portfolio/**").permitAll() // Allow public access to portfolio endpoints
                .requestMatchers("/api/transactions/**").permitAll() // Allow public access to transactions
                .requestMatchers("/api/stocks/**").permitAll() //Allow public access to stock api calls
                .anyRequest().authenticated() // Secure all other endpoints
            )
            .httpBasic(httpBasic -> httpBasic.disable()) // Use basic authentication (or customize as needed)
            .build();
    }
}
