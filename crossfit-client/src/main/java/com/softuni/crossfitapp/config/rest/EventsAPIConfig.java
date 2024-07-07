package com.softuni.crossfitapp.config.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "crossfit.api")
@Getter
@Setter
public class EventsAPIConfig {

    private String mostRecentEvents;
    private String publishUrl;
    private String byIdUrl;
    private String allEvents;
}
