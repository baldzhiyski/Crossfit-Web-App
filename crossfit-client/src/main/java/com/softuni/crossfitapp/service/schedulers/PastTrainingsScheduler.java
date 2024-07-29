package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import com.softuni.crossfitapp.service.WorkoutsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class PastTrainingsScheduler {
    private static final Logger logger = LoggerFactory.getLogger(PastTrainingsScheduler.class);

    private final WorkoutsService workoutsService;

    public PastTrainingsScheduler(WorkoutsService workoutsService) {
        this.workoutsService = workoutsService;
    }

    @Scheduled(cron = "0 0 */6 * * *") // Run every 6 hours
    public void removePastTrainings() {
        LocalDate currentDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        List<WeeklyTraining> pastTrainings = workoutsService.getTrainingsWithDateBefore(currentDate,localTime);
        logger.info("Found {} past trainings. Deleting them now.", pastTrainings.size());

        workoutsService.doDeletion(pastTrainings);
        logger.info("Past trainings deleted successfully.");
    }
}
