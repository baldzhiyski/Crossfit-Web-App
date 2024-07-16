package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.trainings.WeeklyTrainingDto;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.service.schedulers.CreationWeeklyTrainings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Controller
public class WorkoutController {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutController.class);

    private WorkoutsService workoutsService;
    private CommentService commentService;


    private CreationWeeklyTrainings creationWeeklyTrainings;

    public WorkoutController(WorkoutsService workoutsService, CommentService commentService, CreationWeeklyTrainings creationWeeklyTrainings) {
        this.workoutsService = workoutsService;
        this.commentService = commentService;
        this.creationWeeklyTrainings = creationWeeklyTrainings;
    }

    @GetMapping("/workouts")
    public String getWorkouts() {
        return "workouts";
    }

    @GetMapping("/workouts/explore-current/{trainingType}")
    public String getCurrentWorkoutDetails(@PathVariable String trainingType, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        if (!model.containsAttribute("currentTrainingDto")) {
            model.addAttribute("currentTrainingDto", workoutsService.getTrainingDto(TrainingType.valueOf(trainingType)));
        }
        model.addAttribute("displayedCommentsForTheSectionDtos", this.commentService.getAllCommentsOnCurrentTrainingType(TrainingType.valueOf(trainingType)));
        boolean isAuthenticated = currentUser != null;
        model.addAttribute("trainingType", trainingType);
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "workout-details";
    }

    @PostMapping("/workouts/details/comment/{trainingType}")
    public String postComment(@PathVariable String trainingType, AddCommentDto addCommentDto) {
        this.commentService.addComment(addCommentDto, TrainingType.valueOf(trainingType));
        return "redirect:/workouts/explore-current/" + trainingType;
    }

    @GetMapping("/schedule-for-the-week")
    public String schedule(Model model) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (dayOfWeek == DayOfWeek.SUNDAY) break;
            String attributeName = "weeklyTrainings" + dayOfWeek.name();
            List<WeeklyTrainingDto> trainings = this.workoutsService.getWeeklyTrainingsSpecificDay(dayOfWeek);
            model.addAttribute(attributeName, trainings);

            // Logging the attribute name and size of the list
            logger.info("Added attribute '{}' with {} trainings", attributeName, trainings.size());
        }

        return "schedule";
    }

    @PostMapping("/joinTraining/{trainingId}")
    public String joinForCurrentTraining(@PathVariable UUID trainingId,@AuthenticationPrincipal UserDetails userDetails){
        this.workoutsService.joinCurrentTraining(trainingId,userDetails.getUsername());

        return "redirect:/schedule-for-the-week";
    }

    @GetMapping("/my-workouts")
    public String getMyWorkouts(Model model,@AuthenticationPrincipal UserDetails userDetails){
        if(!model.containsAttribute("currentWeeklyTrainings")){
            model.addAttribute("currentWeeklyTrainings",this.workoutsService.getCurrentWeeklyTrainings(userDetails.getUsername()));
        }
        return "my-trainings";
    }

    @PostMapping("/my-workouts/delete-training/{trainingId}")
    public String deleteFromMyTrainings(@PathVariable UUID trainingId,@AuthenticationPrincipal UserDetails userDetails){
        this.workoutsService.deleteFromCurrentTrainings(trainingId,userDetails.getUsername());
        return "redirect:/my-workouts";
    }

}
