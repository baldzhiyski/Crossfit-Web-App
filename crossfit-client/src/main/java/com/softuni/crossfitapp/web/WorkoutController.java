package com.softuni.crossfitapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WorkoutController {

    // TODO : Make dto for this page !
    @GetMapping("/workouts")
    public  String getWorkouts(){
        return "workouts";
    }


    // TODO : With path variable
    @GetMapping("/workouts/details")
    public  String getCurrentWorkoutDetails(){
        return "workout-details";
    }
}
