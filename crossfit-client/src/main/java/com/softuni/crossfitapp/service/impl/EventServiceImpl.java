package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.config.rest.EventsAPIConfig;
import com.softuni.crossfitapp.domain.PageResponse;
import com.softuni.crossfitapp.domain.dto.events.AddEventDto;
import com.softuni.crossfitapp.domain.dto.events.EventDetailsDto;
import com.softuni.crossfitapp.service.EventService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    private RestClient restClient;
    private EventsAPIConfig eventsAPIConfig;

    public EventServiceImpl(@Qualifier("eventsRestClient")RestClient restClient, EventsAPIConfig eventsAPIConfig) {
        this.restClient = restClient;
        this.eventsAPIConfig = eventsAPIConfig;
    }

    @Override
    public void addEvent(AddEventDto eventDetailsDto) {
        restClient.post()
                .uri(this.eventsAPIConfig.getPublishUrl())
                .body(eventDetailsDto)
                .retrieve();

    }

    @Override
    @Cacheable("events")
    public List<EventDetailsDto> getTopThreeEvents() {
       return restClient.get()
                .uri(eventsAPIConfig.getMostRecentEvents())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public Page<EventDetailsDto> getAllEvents(Pageable pageable) {
        String uriString = eventsAPIConfig.getAllEvents();
        PageResponse<EventDetailsDto> events = restClient.get()
                .uri(uriString+ "?page={page}&size={size}&sort=id,asc",
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return new PageImpl<>(events.getContent(), pageable, events.getPage().totalElements());
    }

    @Override
    public EventDetailsDto getEventDetails(UUID id) {

        return restClient
                .get()
                .uri(this.eventsAPIConfig.getByIdUrl(), id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(EventDetailsDto.class);
    }


}
