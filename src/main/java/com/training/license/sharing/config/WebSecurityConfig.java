package com.training.license.sharing.config;

import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Role;
import com.training.license.sharing.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig {
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/")
                        .permitAll()
                    .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(OAuth2LoginConfigurer ->
                        OAuth2LoginConfigurer
                                .successHandler(new AuthenticationSuccessHandler() {

                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                                        String email = oauthUser.getAttribute("email");

                                        Optional<User> optionalUser = userService.findByEmail(email);
                                        if (optionalUser.isPresent()) {
                                            User user = optionalUser.get();
                                            updateAuthenticationWithRoles(user, authentication);
                                        } else {
                                            User newUser = new User();
                                            newUser.setEmail(email);
                                            newUser.setRole(Role.USER);
                                            userService.saveUser(newUser);
                                            updateAuthenticationWithRoles(newUser, authentication);
                                        }
                                    }
                                    private void updateAuthenticationWithRoles(User user, Authentication currentAuthentication) {
                                        String roleName = "ROLE_" + user.getRole().name();
                                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
                                        UsernamePasswordAuthenticationToken updatedAuthentication =
                                                new UsernamePasswordAuthenticationToken(currentAuthentication.getPrincipal(),
                                                        currentAuthentication.getCredentials(),
                                                        Collections.singletonList(authority));
                                        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
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
