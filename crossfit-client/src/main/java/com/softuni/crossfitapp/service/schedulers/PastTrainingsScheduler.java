package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import com.softuni.crossfitapp.service.WorkoutsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class PastTrainingsScheduler {

    private final WorkoutsService workoutsService;

    public PastTrainingsScheduler(WorkoutsService workoutsService) {
        this.workoutsService = workoutsService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Run every day at midnight
    public void removePastTrainings() {
        LocalDate currentDate = LocalDate.now();
        List<WeeklyTraining> pastTrainings = workoutsService.getTrainingsWithDateBefore(currentDate);

        workoutsService.doDeletion(pastTrainings);
    }
}
