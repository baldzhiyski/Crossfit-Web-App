package com.softuni.crossfitcommunityevents.service;


import com.softuni.crossfitcommunityevents.model.dto.EventDto;

import java.util.List;
import java.util.UUID;

public interface EventService {


    EventDto getEventById(UUID id);

    void createEvent(EventDto addEventDto);

    List<EventDto> findMostRecentEvents();

    List<EventDto> findAllEvents();
}
