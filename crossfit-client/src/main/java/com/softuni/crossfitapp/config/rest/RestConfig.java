package com.softuni.crossfitapp.config.rest;

import com.softuni.crossfitapp.service.JwtService;
import com.softuni.crossfitapp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class RestConfig {

    @Bean("genericRestClient")
    public RestClient genericRestClient() {
        return RestClient.create();
    }

    @Bean("trainingsRestClient")
    public RestClient offersRestClient(WorkoutsAPIConfig workoutsAPIConfig) {
        return RestClient
                .builder()
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean("eventsRestClient")
    public RestClient eventsRestClient(EventsAPIConfig eventsAPIConfig, ClientHttpRequestInterceptor requestInterceptor) {
        return RestClient
                .builder()
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(requestInterceptor)
                .build();
    }

    @Bean
    public ClientHttpRequestInterceptor requestInterceptor(UserService userService,
                                                           JwtService jwtService) {
        return (r, b, e) -> {
            // put the logged user details into bearer token
            userService
                    .getCurrentUser()
                    .ifPresent(cud -> {
                        String bearerToken = jwtService.generateToken(
                                cud.getUuid().toString(),//
                                Map.of(
                                        "roles",
                                        cud.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                                        "user",
                                        cud.getUuid().toString()
                                )
                        );
                        r.getHeaders().setBearerAuth(bearerToken);
                    });

            return e.execute(r, b);
        };
    }
}