package com.training.license.sharing.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
            UserDetails user = User.withDefaultPasswordEncoder()
                    .username("a")
                    .password("a")
                    .roles("USER")
                    .build();

            return new InMemoryUserDetailsManager(user);
        }

        @Bean
        public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .requestMatchers("/get-requests")
                    .hasRole("USER")
                    .and().httpBasic();
            return http.build();
        }
}