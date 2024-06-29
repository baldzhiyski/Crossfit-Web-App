package com.softuni.crossfitapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class CoachController {

    @GetMapping("/my-weekly-schedule/{id}")
    public String getMyUpcomingSessions(@PathVariable UUID id){
        return "upcoming-trainings";
    }

    @GetMapping("/coaches")
    public String meetTheTeam(){
        return "coaches-page";
    }
}
