package com.training.license.sharing.config.security;

import com.training.license.sharing.entities.Credential;
import com.training.license.sharing.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CredentialsService credentialsService;
    @Value("${security.endpoints.admin}")
    private String adminEndpoints;

    @Value("${security.endpoints.admin_reviewer}")
    private String adminReviewerEndpoints;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Credential credential = credentialsService.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return User.builder()
                    .username(credential.getUsername())
                    .password(credential.getPassword())
                    .roles(credential.getRole().name())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        List<String> adminPaths = Arrays.asList(adminEndpoints.split(","));
        List<String> adminReviewerPaths = Arrays.asList(adminReviewerEndpoints.split(","));

        http
                .cors()
                .configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.applyPermitDefaultValues();
                    cors.addAllowedOrigin("*");
                    return cors;
                })
                .and()
                .authorizeRequests(auth -> {
                    auth.requestMatchers("/").permitAll();
                    auth.requestMatchers(adminPaths.toArray(new String[0])).hasRole("ADMIN");
                    auth.requestMatchers(adminReviewerPaths.toArray(new String[0])).hasAnyRole("ADMIN", "REVIEWER");
                    auth.anyRequest().authenticated();
                })
                .httpBasic()
                .and()
                .csrf().disable();

        return http.build();
    }
}
