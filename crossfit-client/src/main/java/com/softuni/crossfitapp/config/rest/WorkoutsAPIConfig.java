package com.softuni.crossfitapp.config.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "workouts.api")
@Getter
@Setter
public class WorkoutsAPIConfig {

    private String allTrainingsUrl;
    private String baseUrl;
}
