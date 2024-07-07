package com.softuni.crossfitcommunityevents.web;

import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import com.softuni.crossfitcommunityevents.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/crossfit-community")
public class EventsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsController.class);


    private EventService eventService ;

    public EventsController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/events/find/{id}")
    public ResponseEntity<EventDto> findById(@PathVariable("id") UUID id) {
        return ResponseEntity
                .ok(eventService.getEventById(id));
    }


    @PostMapping("/events/publish")
    public ResponseEntity<EventDto> createOffer(
            @RequestBody EventDto addEventDto
    ) {
        LOGGER.info("Going to create an offer {}", addEventDto);

        eventService.createEvent(addEventDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events/most-recent")
    public ResponseEntity<List<EventDto>> findMostRecentEvents() {
        return ResponseEntity
                .ok(eventService.findMostRecentEvents());
    }

    @GetMapping("/events/all")
    public ResponseEntity<List<EventDto>> findAllEvents() {
        return ResponseEntity
                .ok(eventService.findAllEvents());
    }

}
