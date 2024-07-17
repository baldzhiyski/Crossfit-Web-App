package com.softuni.crossfitcommunityevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class CrossfitCommunityEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrossfitCommunityEventsApplication.class, args);
    }

}
