package com.softuni.crossfitapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableScheduling
@EnableWebSecurity
@EnableCaching
@EnableMethodSecurity
public class CrossfitClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrossfitClientApplication.class, args);
    }

}
