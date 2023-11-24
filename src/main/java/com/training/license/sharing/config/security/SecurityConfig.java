package com.training.license.sharing.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    //ToDo: To delete this class when OAuth story will be ready!
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        final UserDetails user = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.applyPermitDefaultValues();
                    cors.addAllowedOrigin("*");
                    return cors;
                })
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringRequestMatchers("/**")
                .and()
                .httpBasic();
        return http.build();
    }
}