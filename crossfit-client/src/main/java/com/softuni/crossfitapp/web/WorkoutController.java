package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.service.impl.CrossfitUserDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WorkoutController {

    private WorkoutsService workoutsService;

    public WorkoutController(WorkoutsService workoutsService) {
        this.workoutsService = workoutsService;
    }

    @GetMapping("/workouts")
    public  String getWorkouts(){
        return "workouts";
    }

    @GetMapping("/workouts/explore-current/{trainingType}")
    public  String getCurrentWorkoutDetails(@PathVariable String trainingType, Model model,@AuthenticationPrincipal CrossfitUserDetailsService currentUser){
        if(!model.containsAttribute("currentTrainingDto")){
            model.addAttribute("currentTrainingDto",workoutsService.getTrainingDto(TrainingType.valueOf(trainingType)));
        }
        boolean isAuthenticated = currentUser != null;
        model.addAttribute("isAuthenticated",isAuthenticated);
        return "workout-details";
    }


}
