package com.training.license.sharing.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/")
                        .permitAll()
                    .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(OAuth2LoginConfigurer ->
                        OAuth2LoginConfigurer
                                .successHandler(new AuthenticationSuccessHandler() {

                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                                        String email = oauthUser.getAttribute("email");
                                    }
                                })

                );
                return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = CommonOAuth2Provider.GOOGLE.getBuilder("1")
                .clientId("802223075369-03kk3qmuqaika55kr8668d62k89go1mq.apps.googleusercontent.com")
                .clientSecret("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")
                .scope("openid", "profile", "email")
                .build();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

}
