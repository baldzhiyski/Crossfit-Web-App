package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.service.impl.CrossfitUserDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WorkoutController {

    private WorkoutsService workoutsService;
    private CommentService commentService;

    public WorkoutController(WorkoutsService workoutsService, CommentService commentService) {
        this.workoutsService = workoutsService;
        this.commentService = commentService;
    }

    @GetMapping("/workouts")
    public  String getWorkouts(){
        return "workouts";
    }

    @GetMapping("/workouts/explore-current/{trainingType}")
    public  String getCurrentWorkoutDetails(@PathVariable String trainingType, Model model,@AuthenticationPrincipal UserDetails currentUser){
        if(!model.containsAttribute("currentTrainingDto")){
            model.addAttribute("currentTrainingDto",workoutsService.getTrainingDto(TrainingType.valueOf(trainingType)));
        }
        model.addAttribute("displayedCommentsForTheSectionDtos",this.commentService.getAllCommentsOnCurrentTrainingType(TrainingType.valueOf(trainingType)));
        boolean isAuthenticated = currentUser != null;
        model.addAttribute("trainingType",trainingType);
        model.addAttribute("isAuthenticated",isAuthenticated);
        return "workout-details";
    }

    @PostMapping("/workouts/details/comment/{trainingType}")
    public String postComment(@PathVariable String trainingType, AddCommentDto addCommentDto){
        this.commentService.addComment(addCommentDto,TrainingType.valueOf(trainingType));
        return "redirect:/workouts/explore-current/" + trainingType;
    }


}
