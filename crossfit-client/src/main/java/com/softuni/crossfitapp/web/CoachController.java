package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.service.CoachService;
import com.softuni.crossfitapp.service.WorkoutsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class CoachController {
    private CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/coaches")
    public String meetTheTeam(Model model){
        model.addAttribute("coachesDisplayDtos",coachService.getCoachesInfoForMeetTheTeamPage());
        return "coaches";
    }

    // This is only for the coaches . They can remove a training if they do not feel well . In this case the training should be remove !
    @GetMapping("/my-weekly-schedule/{username}")
    public String getMyWeeklyTrainingsSchedule(@PathVariable String username, Model model){
        if(!model.containsAttribute("upcomingTrainings")){
            model.addAttribute("upcomingTrainings",this.coachService.getUpcomingTrainings(username));
        }
        return "upcoming-trainings";
    }


    @PostMapping("/my-weekly-schedule/{username}/cancel-training/{weeklyTrainingId}")
    public String cancelTrainig(@PathVariable String username,@PathVariable UUID weeklyTrainingId){
        this.coachService.closeTrainingSession(username,weeklyTrainingId);

        return "redirect:/my-weekly-schedule/" + username;
    }
}
