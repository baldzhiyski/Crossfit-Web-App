package com.softuni.crossfitapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class EventController {

    @GetMapping("/events/add-event/{id}")
    public String getEventPage(@PathVariable UUID id){
        return "add-event";
    }
}
