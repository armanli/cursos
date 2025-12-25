package com.example.student_address.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(auth ->
                        auth.requestMatchers("/login", "/error").permitAll().
                                requestMatchers("/students/**", "/address/**").hasAnyRole().
                                anyRequest().authenticated()
                ).formLogin(login ->
                        login.loginPage("login").defaultSuccessUrl("/address", true).permitAll()).
                logout(logout -> logout.logoutUrl("logout").logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }
}
