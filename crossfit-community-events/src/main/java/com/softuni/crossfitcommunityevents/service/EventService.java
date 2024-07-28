package com.softuni.crossfitcommunityevents.service;


import com.softuni.crossfitcommunityevents.model.dto.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface EventService {


    EventDto getEventById(Long id);

    void createEvent(EventDto addEventDto);

    List<EventDto> findSomeRandomEvents();
    PagedModel<EventDto> findAllEvents(Pageable pageable);

    boolean deleteEvent(Long id);
}
