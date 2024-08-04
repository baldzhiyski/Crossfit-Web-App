package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.service.WorkoutsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class CreationWeeklyTrainings {

    private WorkoutsService workoutsService;

    private UserService userService;

    private final Logger LOGGER = LoggerFactory.getLogger(CreationWeeklyTrainings.class);


    public CreationWeeklyTrainings(WorkoutsService workoutsService, UserService userService) {
        this.workoutsService = workoutsService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 0 ? * 7") // Runs at midnight at the end of Saturday
    public void createAndDeleteWeeklyTrainings() {
        // Populate new weekly trainings
        workoutsService.populateWeeklyTrainings();
        LOGGER.info("Populating new trainings for the week");

        // Reset for each user the allowed trainings per week
        userService.resetAllowedTrainingsPerWeek();
        LOGGER.info("Reset for each user depending on the membership the allowed trainings ");

    }
}
