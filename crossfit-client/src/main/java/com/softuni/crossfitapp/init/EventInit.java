package com.softuni.crossfitapp.init;

import com.softuni.crossfitapp.service.EventService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "crossfit.api.init-crossfit-events", havingValue = "true")
public class EventInit implements CommandLineRunner {
    private final EventService eventService;

    public EventInit(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
