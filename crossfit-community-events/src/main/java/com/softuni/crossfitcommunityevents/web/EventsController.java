package com.softuni.crossfitcommunityevents.web;

import com.softuni.crossfitcommunityevents.exception.ObjectNotFoundException;
import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import com.softuni.crossfitcommunityevents.service.EventService;
import com.softuni.crossfitcommunityevents.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/crossfit-community")
@CrossOrigin(origins = "http://localhost:8080")
@Tag(name = "Events API", description = "API for managing CrossFit community events, including creation, retrieval, and listing.")
public class EventsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsController.class);

    private final EventService eventService;

    private MonitoringService monitoringService;

    public EventsController(EventService eventService, MonitoringService monitoringService) {
        this.eventService = eventService;
        this.monitoringService = monitoringService;
    }

    @Operation(
            summary = "Delete an event by ID",
            description = "Delete a specific event using its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Event deleted successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Event not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Error Response",
                                            summary = "Error response for event not found",
                                            description = "This error occurs when the event ID does not exist.",
                                            value = "{\"message\":\"Event not found\"}"
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/events/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        LOGGER.info("Deleting event with id {}", id);
        eventService.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Retrieve event details by ID",
            description = "Fetch the details of a specific event using its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Event details retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventDto.class),
                                    examples = @ExampleObject(
                                            name = "Sample Event",
                                            summary = "A sample event",
                                            description = "This is a sample event description.",
                                            value = "{\"eventName\":\"CrossFit Open\",\"description\":\"An open event for all fitness levels.\",\"date\":\"2024-07-25T10:00:00\",\"location\":\"CrossFit Box\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Event not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Error Response",
                                            summary = "Error response for event not found",
                                            description = "This error occurs when the event ID does not exist.",
                                            value = "{\"message\":\"Event not found\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping(value = "/events/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> findById(@PathVariable("id") Long id) {
        EventDto eventDto = eventService.getEventById(id);
        if (eventDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventDto);
    }

    @Operation(
            summary = "Publish a new event",
            description = "Create and publish a new event within the CrossFit community.",
            requestBody = @RequestBody(
                    description = "Details of the event to be created, including name, description, date, and location.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventDto.class),
                            examples = @ExampleObject(
                                    name = "New Event Request",
                                    summary = "Example of a new event creation request",
                                    value = "{\"eventName\":\"CrossFit Challenge\",\"description\":\"A challenging event for advanced athletes.\",\"date\":\"2024-08-01T09:00:00\",\"location\":\"Main Gym\"}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Event created successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventDto.class),
                                    examples = @ExampleObject(
                                            name = "Created Event",
                                            summary = "Example of a successfully created event",
                                            value = "{\"eventName\":\"CrossFit Challenge\",\"description\":\"A challenging event for advanced athletes.\",\"date\":\"2024-08-01T09:00:00\",\"location\":\"Main Gym\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid event data provided.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Error Response",
                                            summary = "Error response for invalid data",
                                            description = "Occurs when the provided event data is invalid or incomplete.",
                                            value = "{\"message\":\"Invalid event data\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/events/publish")
    public ResponseEntity<EventDto> createEvent(
            @org.springframework.web.bind.annotation.RequestBody EventDto addEventDto) {
        LOGGER.info("Creating event {}", addEventDto);
        eventService.createEvent(addEventDto);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .buildAndExpand(addEventDto.getEventName())
                        .toUri()
        ).body(addEventDto);
    }

    @Operation(
            summary = "Retrieve most recent events",
            description = "Get a list of the most recent events available in the community.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Most recent events retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventDto.class)
                            )
                    )
            }
    )
    @GetMapping("/events/most-recent")
    public ResponseEntity<List<EventDto>> findMostRecentEvents() {
        return ResponseEntity.ok(eventService.findSomeRandomEvents());
    }

    @Operation(
            summary = "List all events with pagination",
            description = "Retrieve a paginated list of all events, sorted by date.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "page",
                            description = "Page number of the results to retrieve.",
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "size",
                            description = "Number of results per page.",
                            schema = @Schema(type = "integer", defaultValue = "3")
                    ),
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "sort",
                            description = "Sorting criteria: field name and direction (asc/desc).",
                            schema = @Schema(type = "string", example = "date,asc")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of events retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    )
            }
    )
    @GetMapping("/events/all")
    public ResponseEntity<PagedModel<EventDto>> findAllEvents(
            @PageableDefault(size = 3, sort = "date", direction = Sort.Direction.ASC) Pageable pageable) {
        monitoringService.increaseEventsSearch();
        return ResponseEntity.ok(eventService.findAllEvents(pageable));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, object not found.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error Response",
                                    summary = "Error response for object not found",
                                    description = "This error occurs when the requested object does not exist.",
                                    value = "{\"message\":\"Object not found\"}"
                            )
                    )
            )
    })
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        LOGGER.error("ObjectNotFoundException caught: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
