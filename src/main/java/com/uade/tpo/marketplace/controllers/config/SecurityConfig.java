package com.uade.tpo.marketplace.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req -> req
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/products").permitAll()

            .requestMatchers(HttpMethod.GET, "/categories/**", "/images/**", "/products/**")
                .hasAnyRole("USER","SELLER","ADMIN")

            .requestMatchers("/carts/**").hasRole("USER")
            .requestMatchers("/orders/user/**").hasRole("USER")

            .requestMatchers(HttpMethod.POST,   "/products/**","/images/**","/categories/**").hasRole("SELLER")
            .requestMatchers(HttpMethod.PUT,    "/products/**","/images/**","/categories/**").hasRole("SELLER")
            .requestMatchers(HttpMethod.DELETE, "/products/**","/images/**","/categories/**").hasRole("SELLER")
            .requestMatchers(HttpMethod.GET, "/users/me").hasRole("USER")
            .requestMatchers(HttpMethod.PUT, "/users/me").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/users/**","/orders/admin/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/users/**","/products/**","/categories/**").hasRole("ADMIN")

            .anyRequest().authenticated()
        )
        .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }
}
