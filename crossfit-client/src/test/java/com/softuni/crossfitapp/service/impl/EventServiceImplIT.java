package com.softuni.crossfitapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import com.softuni.crossfitapp.config.rest.EventsAPIConfig;
import com.softuni.crossfitapp.domain.dto.events.AddEventDto;
import com.softuni.crossfitapp.domain.dto.events.EventDetailsDto;
import com.softuni.crossfitapp.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock(
        @ConfigureWireMock(name = "events")
)
public class EventServiceImplIT {

    @InjectWireMock("events")
    private WireMockServer wireMockServer;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventsAPIConfig eventsAPIConfig;

    @Autowired
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        eventsAPIConfig.setPublishUrl(wireMockServer.baseUrl() + "/events/publish");
        eventsAPIConfig.setMostRecentEvents(wireMockServer.baseUrl() + "/events/top");
        eventsAPIConfig.setByIdUrl(wireMockServer.baseUrl() + "/events/{id}");
        eventsAPIConfig.setAllEvents(wireMockServer.baseUrl() +"/events/all");
    }

    @Test
    void testAddEvent() throws JsonProcessingException {
        // Mock event data to be sent
        AddEventDto eventDetailsDto = new AddEventDto();
        eventDetailsDto.setEventName("Test Event");
        eventDetailsDto.setDescription("Description of Test Event");
        // Set other properties as needed

        // Convert the AddEventDto to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(eventDetailsDto);

        // Stub WireMock server to accept the event
        wireMockServer.stubFor(post("/events/publish")
                .withRequestBody(WireMock.equalToJson(requestJson))
                .willReturn(aResponse().withStatus(201)));

        // Execute the service method
        eventService.addEvent(eventDetailsDto);

    }

    @Test
    void testGetTopThreeEvents() throws JsonProcessingException {

        EventDetailsDto[] mockEventDetailsDtos = {
                new EventDetailsDto( "Description 1",new Date(2), "Address 1","Event 1","url1"),
                new EventDetailsDto( "Description 2", new Date(2), "Address 2","Event 2","url2"),
                new EventDetailsDto( "Description 3",new Date(2), "Address 3","Event 3","url3")
        };

        // Convert the mockEventDetailsDtos array to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String mockResponse = objectMapper.writeValueAsString(mockEventDetailsDtos);

        // Stub WireMock server to return mock response
        wireMockServer.stubFor(get("/events/top")
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mockResponse)));

        // Execute the service method
        List<EventDetailsDto> events = eventService.getTopThreeEvents();

        // Assertions
        assertEquals(3, events.size());
        assertEquals("Event 1", events.get(0).getEventName());
        assertEquals("Event 2", events.get(1).getEventName());
        assertEquals("Event 3", events.get(2).getEventName());
    }

    @Test
    void testGetEventDetails() throws JsonProcessingException {
        // Mock event data
        UUID eventId = UUID.randomUUID();
        EventDetailsDto eventDto = new EventDetailsDto("Description 1", new Date(2), "Address 1", "Event 1", "url1");



        // Convert the mockEventDetails to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String mockResponse = objectMapper.writeValueAsString(eventDto);

        // Stub WireMock server to return mock response
        wireMockServer.stubFor(get("/events/" + eventId)
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mockResponse)));

        // Execute the service method
        EventDetailsDto eventDetails = eventService.getEventDetails(eventId);

        // Assertions
        assertNotNull(eventDetails);
        assertEquals("Event 1", eventDetails.getEventName());
        assertEquals("Description 1", eventDetails.getDescription());
    }
}
