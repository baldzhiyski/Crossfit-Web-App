package com.softuni.crossfitapp.config.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
public class TestRestConfig {

    @ConditionalOnProperty(value="rest.configuration", havingValue="test")
    @Bean("eventsRestClient")
    public RestClient restClient() {

        var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        var requestFactory = new JdkClientHttpRequestFactory(client);

        return RestClient
                .builder()
                .requestFactory(requestFactory)
                .build();
    }

}