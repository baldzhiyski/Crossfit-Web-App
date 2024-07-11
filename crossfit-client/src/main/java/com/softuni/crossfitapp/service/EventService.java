package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.events.AddEventDto;
import com.softuni.crossfitapp.domain.dto.events.EventDetailsDto;

import java.util.List;
import java.util.UUID;

public interface EventService {

    void addEvent(AddEventDto eventDetailsDto);

    List<EventDetailsDto> getTopThreeEvents();
    List<EventDetailsDto> getAllEvents();

    EventDetailsDto getEventDetails(UUID id);
}
