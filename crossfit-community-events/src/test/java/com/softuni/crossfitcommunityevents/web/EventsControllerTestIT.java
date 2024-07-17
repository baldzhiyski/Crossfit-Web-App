package com.softuni.crossfitcommunityevents.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.softuni.crossfitcommunityevents.model.Event;
import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import com.softuni.crossfitcommunityevents.repository.EventRepository;
import com.softuni.crossfitcommunityevents.service.EventService;
import com.softuni.crossfitcommunityevents.service.impl.EventServiceImpl;
import com.softuni.crossfitcommunityevents.util.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
class EventsControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData testData;


    @Autowired
    private EventRepository eventRepository;


    private EventService eventService;


    @BeforeEach
    public void setUp(){
        this.eventService= new EventServiceImpl(eventRepository);
    }

    @AfterEach
    public void clean() {
        this.eventRepository.deleteAll();
    }


    @Test
    @Transactional
    public void testFindById() throws Exception {
        // Assume testData.createEvent creates an Event with a valid id
        Event testEvent = new Event("Test", "Test description", "Some Address", new Date(2), "some_url");

        eventRepository.saveAndFlush(testEvent);

        // Perform the request and validate
        ResultActions result = mockMvc.perform(get("/crossfit-community/events/find/{id}", testEvent.getId())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", is(testEvent.getDescription())))
                .andExpect(jsonPath("$.eventName", is(testEvent.getEventName())))
                .andExpect(jsonPath("$.videoUrl", is(testEvent.getVideoUrl())));
    }

    // Utility method to convert Event to EventDto
    private EventDto convertToEventDto(Event event) {
        EventDto dto = new EventDto();
        dto.setEventName(event.getEventName());
        dto.setDescription(event.getDescription());
        dto.setAddress(event.getAddress());
        dto.setDate(event.getDate());
        dto.setVideoUrl(event.getVideoUrl());
        return dto;
    }

    @Test
    public void testFindByIdFails() throws Exception {
        mockMvc.perform(get("/crossfit-community/events/find/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }


    @Test
    public void createEvent() throws Exception {
        MvcResult result = mockMvc.perform(post("/crossfit-community/events/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "description": "Test description",
                                    "eventName": "Test",
                                    "address": "Some Address",
                                    "videoUrl": "some_url",
                                    "date" : "2025-08-01T00:00:00.000+00:00"
                                  }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        String eventName = JsonPath.read(body, "$.eventName");

        Optional<Event> createdEvent = eventRepository.findByEventName("Test");
        assertTrue(createdEvent.isPresent());

        // Assert the details of the created event
        Event event = createdEvent.get();
        Assertions.assertEquals("Test description", event.getDescription(), "Description should match");
        Assertions.assertEquals("Test", event.getEventName(), "Event name should match");
        Assertions.assertEquals("Some Address", event.getAddress(), "Address should match");
        Assertions.assertEquals("some_url", event.getVideoUrl(), "Video URL should match");

    }

    @Test
    public void testFindSomeRandomEvents() throws Exception {
        // Given
        Event event1 = new Event("Event 1", "Description 1", "Address 1", new Date(2), "url1");
        Event event2 = new Event("Event 2", "Description 2", "Address 2", new Date(2), "url2");
        Event event3 = new Event("Event 3", "Description 3", "Address 3", new Date(2), "url3");
        Event event4 = new Event("Event 4", "Description 3", "Address 3", new Date(2), "url3");
        Event event5 = new Event("Event 5", "Description 3", "Address 3", new Date(2), "url3");
        Event event6 = new Event("Event 6", "Description 3", "Address 3", new Date(2), "url3");
        Event event7 = new Event("Event 7", "Description 3", "Address 3", new Date(2), "url3");
        List<Event> events = List.of(event1, event2, event3, event4, event5, event6, event7);
        eventRepository.saveAllAndFlush(events);

        ResultActions result = mockMvc.perform(get("/crossfit-community/events/most-recent")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void testGetAllEvents() throws Exception {
        // Given
        Event event1 = new Event("Event 1", "Description 1", "Address 1", new Date(2), "url1");
        Event event2 = new Event("Event 2", "Description 2", "Address 2", new Date(2), "url2");
        Event event3 = new Event("Event 3", "Description 3", "Address 3", new Date(2), "url3");
        Event event4 = new Event("Event 4", "Description 3", "Address 3", new Date(2), "url3");
        Event event5 = new Event("Event 5", "Description 3", "Address 3", new Date(2), "url3");
        Event event6 = new Event("Event 6", "Description 3", "Address 3", new Date(2), "url3");
        Event event7 = new Event("Event 7", "Description 3", "Address 3", new Date(2), "url3");
        List<Event> events = List.of(event1, event2, event3, event4, event5, event6, event7);

        Pageable pageable = PageRequest.of(0, 3, Sort.by("date").ascending());
        Page<Event> eventPage = new PageImpl<>(events.subList(0, 3), pageable, events.size());

        this.eventRepository.saveAllAndFlush(events);

        // When & Then
        MvcResult result = mockMvc.perform(get("/crossfit-community/events/all")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "date,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert the content of the response body
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + contentAsString);

        // Verify pagination information by checking if specific elements are present in the response string
        assertTrue(contentAsString.contains("\"pageNumber\":0"), "Page number should be 0 in the response");
        assertTrue(contentAsString.contains("\"pageSize\":3"), "Page size should be 3 in the response");

        // You can add more assertions here to verify the content of the events returned on the first page
        assertTrue(contentAsString.contains("\"content\":"), "Response should contain 'content' array");
        assertTrue(contentAsString.contains("\"eventName\":\"Event 1\""), "Event 1 should be in the response");
        assertTrue(contentAsString.contains("\"eventName\":\"Event 2\""), "Event 2 should be in the response");
        assertTrue(contentAsString.contains("\"eventName\":\"Event 3\""), "Event 3 should be in the response");

    }
}