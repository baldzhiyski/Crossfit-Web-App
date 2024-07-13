package com.softuni.crossfitcommunityevents.service;


import com.softuni.crossfitcommunityevents.model.dto.EventDto;

import java.util.List;

public interface EventService {


    EventDto getEventById(Long id);

    void createEvent(EventDto addEventDto);

    List<EventDto> findSomeRandomEvents();

    List<EventDto> findAllEvents();
}
