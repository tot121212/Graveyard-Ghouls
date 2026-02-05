package com.totsnuk.graveyardghouls.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll())
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionConcurrency((sessionConcurrency) -> sessionConcurrency
                                .maximumSessions(1)
                                .expiredUrl("/?expired")))
                .csrf((csrf) -> csrf.disable())
                .cors((cors) -> cors.disable());

        return http.build();
    }

}
