package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.events.AddEventDto;
import com.softuni.crossfitapp.domain.dto.events.EventDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EventService {

    void addEvent(AddEventDto eventDetailsDto);

    List<EventDetailsDto> getTopThreeEvents();
    Page<EventDetailsDto> getAllEvents(Pageable pageable);

    EventDetailsDto getEventDetails(UUID id);
}
