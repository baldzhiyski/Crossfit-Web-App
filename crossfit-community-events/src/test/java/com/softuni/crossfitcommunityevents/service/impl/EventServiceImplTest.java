package com.softuni.crossfitcommunityevents.service.impl;

import com.softuni.crossfitcommunityevents.exception.ObjectNotFoundException;
import com.softuni.crossfitcommunityevents.model.Event;
import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import com.softuni.crossfitcommunityevents.repository.EventRepository;
import com.softuni.crossfitcommunityevents.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private  EventRepository eventRepository;

    private EventService eventService;

    @Captor
    private ArgumentCaptor<Event> eventArgumentCaptor;

    @BeforeEach
    public void init(){
        this.eventService = new EventServiceImpl(eventRepository);
    }

    @Test
    void testGetEventById_EventExists() {
        Long eventId = 1L;
        Event event = new Event();
        event.setEventName("Test Event");
        event.setId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventDto eventDto = eventService.getEventById(eventId);

        assertNotNull(eventDto);
        assertEquals("Test Event", eventDto.getEventName());
        verify(eventRepository, times(1)).findById(eventId);
    }


    @Test
    void testGetEventById_EventDoesNotExist() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> eventService.getEventById(eventId));
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void testCreateEvent() {
        EventDto eventDto = EventDto.builder()
                .eventName("New Event")
                .address("123 Main St")
                .description("Description of new event")
                .date(new Date(2))
                .videoUrl("http://example.com/video")
                .build();

        eventService.createEvent(eventDto);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository, times(1)).saveAndFlush(eventCaptor.capture());

        Event savedEvent = eventCaptor.getValue();
        assertEquals("New Event", savedEvent.getEventName());
        assertEquals("123 Main St", savedEvent.getAddress());
        assertEquals("Description of new event", savedEvent.getDescription());
        assertEquals(eventDto.getDate(), savedEvent.getDate());
        assertEquals("http://example.com/video", savedEvent.getVideoUrl());
    }

    @Test
    void testFindSomeRandomEvents() {
        Event event1 = new Event();
        event1.setEventName("Event 1");
        Event event2 = new Event();
        event2.setEventName("Event 2");
        Event event3 = new Event();
        event3.setEventName("Event 3");
        Event event4 = new Event();
        event4.setEventName("Event 4");

        when(eventRepository.findSomeRandomEvents()).thenReturn(List.of(event1, event2, event3, event4));

        List<EventDto> events = eventService.findSomeRandomEvents();

        assertNotNull(events);
        assertEquals(3, events.size());
    }

    @Test
    void testFindAllEvents() {
        // Create mock data
        Event event1 = new Event();
        event1.setEventName("Event 1");
        Event event2 = new Event();
        event2.setEventName("Event 2");

        Page<Event> eventPage = new PageImpl<>(List.of(event1, event2));

        Pageable pageable = PageRequest.of(0, 3, Sort.by("date").ascending());

        when(eventRepository.findAll(pageable)).thenReturn(eventPage);
        Page<EventDto> events = eventService.findAllEvents(pageable);

        // Assertions
        assertNotNull(events);
        assertEquals(2, events.getTotalElements());
        assertEquals("Event 1", events.getContent().get(0).getEventName());
        assertEquals("Event 2", events.getContent().get(1).getEventName());
    }


}