package com.example.Assignment.configs;

import com.example.Assignment.filters.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable());
        http
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                );
        http.authorizeHttpRequests(
                request -> request
                        // we will later change the setting for h2
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/signup").permitAll()
                        .requestMatchers("/api/signin").permitAll()
                        .requestMatchers("/api/refresh_token").authenticated()
                        .requestMatchers("/api/products/**").authenticated()
                        .requestMatchers("/api/buy/**").authenticated()
        );
        http.headers().frameOptions().sameOrigin();
        http.httpBasic(httpSecurityHttpBasicConfigurer -> {});
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
