package com.softuni.crossfitcommunityevents.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize ->
                                authorize.requestMatchers(HttpMethod.GET, "/crossfit-community/**","/swagger-ui/**", "swagger-ui.html", "/v3/api-docs/**").permitAll()
                                        .requestMatchers(HttpMethod.POST,"/crossfit-community/events/publish").permitAll()
                                        .requestMatchers(HttpMethod.DELETE,"/crossfit-community/events/delete/{id}").permitAll()
                                        .anyRequest().authenticated()
                )
                .build();

    }
}
