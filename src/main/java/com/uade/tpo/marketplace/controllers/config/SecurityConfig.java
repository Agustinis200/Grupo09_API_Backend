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
import java.util.List;
import org.springframework.web.cors.CorsConfiguration;
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
        .cors(cors -> cors.configurationSource(request -> {
            var corsConfig = new CorsConfiguration();
            corsConfig.setAllowedOrigins(List.of("http://localhost:5174"));
            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfig.setAllowedHeaders(List.of("*"));
            corsConfig.setAllowCredentials(true);
            return corsConfig;
        }))
        .authorizeHttpRequests(req -> req
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/products","/categories").permitAll()

            .requestMatchers(HttpMethod.GET, "/categories/**", "/images/**", "/products/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/me").permitAll()

            .requestMatchers("/carts/**").hasRole("USER")
            .requestMatchers("/orders/me/**").hasRole("USER")
            
            .requestMatchers(HttpMethod.DELETE, "/products/**").hasAnyRole("SELLER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/products/**","/images/**","/categories/**").hasRole("SELLER")
            .requestMatchers(HttpMethod.PUT, "/products/**","/images/**","/categories/**").hasRole("SELLER")
            .requestMatchers(HttpMethod.DELETE, "/images/**","/categories/**").hasAnyRole("SELLER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/users/me").hasAnyRole("USER", "SELLER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/probador/**").hasRole("USER")
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
