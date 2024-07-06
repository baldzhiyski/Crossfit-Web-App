package com.softuni.crossfitapp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "countries.api")
public class CountriesNowConfig {

    private String url;

    public String getUrl() {
        return url;
    }

    public CountriesNowConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    @PostConstruct
    public void checkConfiguration() {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Url cannot be empty.");
        }

    }
}
