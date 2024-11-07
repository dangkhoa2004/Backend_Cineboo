package com.backend.cineboo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/swagger-config",
                        "/api-docs",
                        "/swagger-resources/**",
                        "/swagger-resources",
                        "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/payos/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/css/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/payos/test").permitAll()
                .requestMatchers(HttpMethod.GET,"/payos/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/payos/test").permitAll()
                .requestMatchers(HttpMethod.POST, "api/user/login").permitAll()
                .requestMatchers(HttpMethod.GET, "phim/get").permitAll()
                .requestMatchers(HttpMethod.GET, "/phim/get").permitAll()
                .requestMatchers(HttpMethod.PUT, "phim/disable").permitAll()
                .requestMatchers(HttpMethod.PUT, "phim/disable/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "phim/update/id").permitAll()
                .requestMatchers(HttpMethod.PUT, "phim/update/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/phim/find/**").permitAll()
                .requestMatchers(HttpMethod.POST, "phim/add").permitAll()
                .requestMatchers(HttpMethod.PUT, "phim/update").permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
