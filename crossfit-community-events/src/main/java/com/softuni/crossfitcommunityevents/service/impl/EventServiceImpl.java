package com.softuni.crossfitcommunityevents.service.impl;

import com.softuni.crossfitcommunityevents.exception.ObjectNotFoundException;
import com.softuni.crossfitcommunityevents.model.Event;
import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import com.softuni.crossfitcommunityevents.repository.EventRepository;
import com.softuni.crossfitcommunityevents.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDto getEventById(Long id) {
        Event event = this.eventRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("No such upcomming event !"));

        return mapToDto(event);
    }

    @Override
    @Transactional
    public void createEvent(EventDto addEventDto) {
        Event event = mapToEntity(addEventDto);
        this.eventRepository.saveAndFlush(event);
    }

    @Override
    public List<EventDto> findSomeRandomEvents() {
        return eventRepository.findSomeRandomEvents()
                .stream().limit(3).map(this::mapToDto).toList();

    }

    @Override
    public Page<EventDto> findAllEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(this::mapToDto);
    }

    private EventDto mapToDto(Event event) {
        return EventDto.builder()
                .eventName(event.getEventName())
                .address(event.getAddress())
                .date(event.getDate())
                .description(event.getDescription())
                .videoUrl(event.getVideoUrl())
                .build();
    }
    private Event mapToEntity(EventDto eventDto) {
        return Event.builder()
                .eventName(eventDto.getEventName())
                .address(eventDto.getAddress())
                .description(eventDto.getDescription())
                .date(eventDto.getDate())
                .videoUrl(eventDto.getVideoUrl())
                .build();
    }


}
