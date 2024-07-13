package com.softuni.crossfitcommunityevents.web;

import com.jayway.jsonpath.JsonPath;
import com.softuni.crossfitcommunityevents.model.Event;
import com.softuni.crossfitcommunityevents.repository.EventRepository;
import com.softuni.crossfitcommunityevents.service.EventService;
import com.softuni.crossfitcommunityevents.util.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class EventsControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData testData;

    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    public void clean(){
        this.eventRepository.deleteAll();
    }


    @Test
    public void testFindById() throws Exception {

        Event testEvent = this.testData.createEvent("Test", "Test description", new Date(2), "Some Address", "some_url");

        ResultActions result = mockMvc.perform(get("/crossfit-community/events/find/{id}", testEvent.getId())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", is(testEvent.getDescription())))
                .andExpect(jsonPath("$.eventName", is(testEvent.getEventName())))
                .andExpect(jsonPath("$.videoUrl", is(testEvent.getVideoUrl())));
    }

    @Test
    public  void testFindByIdFails() throws Exception {
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
        String  eventName = JsonPath.read(body, "$.eventName");
        Optional<Event> createdEvent = eventRepository.findByEventName(eventName);
        Assertions.assertTrue(createdEvent.isPresent());

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
        Event event1 = new Event( "Event 1", "Description 1", "Address 1",new Date(2),"url1");
        Event event2 = new Event( "Event 2", "Description 2", "Address 2",new Date(2), "url2");
        Event event3 = new Event("Event 3", "Description 3", "Address 3", new Date(2),"url3");
        Event event4 = new Event("Event 4", "Description 3", "Address 3", new Date(2),"url3");
        Event event5 = new Event("Event 5", "Description 3", "Address 3", new Date(2),"url3");
        Event event6 = new Event("Event 6", "Description 3", "Address 3", new Date(2),"url3");
        Event event7 = new Event("Event 7", "Description 3", "Address 3", new Date(2),"url3");
        eventRepository.saveAll(List.of(event1, event2, event3,event4,event5,event6,event7));

        // When
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
        Event event1 = new Event( "Event 1", "Description 1", "Address 1",new Date(2),"url1");
        Event event2 = new Event( "Event 2", "Description 2", "Address 2",new Date(2), "url2");
        Event event3 = new Event("Event 3", "Description 3", "Address 3", new Date(2),"url3");
        Event event4 = new Event("Event 4", "Description 3", "Address 3", new Date(2),"url3");
        Event event5 = new Event("Event 5", "Description 3", "Address 3", new Date(2),"url3");
        Event event6 = new Event("Event 6", "Description 3", "Address 3", new Date(2),"url3");
        Event event7 = new Event("Event 7", "Description 3", "Address 3", new Date(2),"url3");
        eventRepository.saveAll(List.of(event1, event2, event3,event4,event5,event6,event7));

        // When
        ResultActions result = mockMvc.perform(get("/crossfit-community/events/all")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(7));
    }



}