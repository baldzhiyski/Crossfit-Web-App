package com.softuni.crossfitcommunityevents.util;

import com.softuni.crossfitcommunityevents.model.Event;
import com.softuni.crossfitcommunityevents.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestData {


    private EventRepository eventRepository;

    public TestData(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    public Event createEvent(String eventName, String description, Date date, String address,String videoUrl){
        Event event = Event.builder().eventName(eventName)
                .description(description)
                .date(date)
                .address(address)
                .videoUrl(videoUrl)
                .build();

         return this.eventRepository.saveAndFlush(event);
    }
}
