package com.softuni.crossfitapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrossfitClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrossfitClientApplication.class, args);
    }

}
