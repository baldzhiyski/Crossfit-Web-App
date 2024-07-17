package com.softuni.crossfitcommunityevents.service;


import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {


    EventDto getEventById(Long id);

    void createEvent(EventDto addEventDto);

    List<EventDto> findSomeRandomEvents();
    Page<EventDto> findAllEvents(Pageable pageable);
}
