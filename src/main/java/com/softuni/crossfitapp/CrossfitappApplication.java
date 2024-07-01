package com.softuni.crossfitapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CrossfitappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrossfitappApplication.class, args);
	}

}
