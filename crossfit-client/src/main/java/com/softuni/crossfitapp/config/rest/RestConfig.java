package com.softuni.crossfitapp.config.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

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
    public RestClient eventsRestClient(EventsAPIConfig eventsAPIConfig) {
        return RestClient
                .builder()
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}