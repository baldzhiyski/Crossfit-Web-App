package com.softuni.crossfitcommunityevents.service.impl;

import com.softuni.crossfitcommunityevents.model.Event;
import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import com.softuni.crossfitcommunityevents.repository.EventRepository;
import com.softuni.crossfitcommunityevents.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDto getEventById(UUID id) {
        Event event = this.eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such upcomming event !"));

        return mapToDto(event);
    }

    @Override
    public void createEvent(EventDto addEventDto) {
        Event event = mapToEntity(addEventDto);
        this.eventRepository.save(event);
    }

    @Override
    public List<EventDto> findMostRecentEvents() {
        return eventRepository.findTop3ByOrderByDateDesc()
                .stream().map(this::mapToDto).toList();

    }

    @Override
    public List<EventDto> findAllEvents() {
        return this.eventRepository.findAll().stream().map(this::mapToDto).toList();
    }

    private EventDto mapToDto(Event event) {
        return EventDto.builder()
                .eventName(event.getEventName())
                .address(event.getAddress())
                .date(event.getDate())
                .description(event.getDescription())
                .build();
    }
    private Event mapToEntity(EventDto eventDto) {
        return Event.builder()
                .eventName(eventDto.getEventName())
                .address(eventDto.getAddress())
                .description(eventDto.getDescription())
                .date(eventDto.getDate())
                .build();
    }


}
