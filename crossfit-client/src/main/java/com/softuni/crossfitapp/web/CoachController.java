package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.service.CoachService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class CoachController {
    private CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/my-weekly-schedule/{id}")
    public String getMyUpcomingSessions(@PathVariable UUID id){
        return "upcoming-trainings";
    }

    @GetMapping("/coaches")
    public String meetTheTeam(Model model){
        model.addAttribute("coachesDisplayDtos",coachService.getCoachesInfoForMeetTheTeamPage());
        return "coaches";
    }
}
