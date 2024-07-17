package com.softuni.crossfitcommunityevents.web;

import com.softuni.crossfitcommunityevents.exception.ObjectNotFoundException;
import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import com.softuni.crossfitcommunityevents.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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


    @GetMapping(value = "/events/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> findById(@PathVariable("id") Long id) {
        EventDto eventDto = eventService.getEventById(id);
        if (eventDto == null) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        }
        return ResponseEntity.ok(eventDto);
    }

    @PostMapping("/events/publish")
    public ResponseEntity<EventDto> createEvent(
            @RequestBody EventDto addEventDto
    ) {
        LOGGER.info("Going to create an offer {}", addEventDto);
        System.out.println("Starting");

        eventService.createEvent(addEventDto);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .buildAndExpand(addEventDto.getEventName())
                        .toUri()
        ).body(addEventDto);
    }

    @GetMapping("/events/most-recent")
    public ResponseEntity<List<EventDto>> findMostRecentEvents() {
        return ResponseEntity
                .ok(eventService.findSomeRandomEvents());
    }

    @GetMapping("/events/all")
    public ResponseEntity<Page<EventDto>> findAllEvents(@PageableDefault(
            size = 3,
            sort = "date",
            direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity
                .ok(eventService.findAllEvents(pageable));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        LOGGER.error("ObjectNotFoundException caught: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
