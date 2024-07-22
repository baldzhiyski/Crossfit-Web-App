package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.service.WorkoutsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class CreationWeeklyTrainings {

    private WorkoutsService workoutsService;


    public CreationWeeklyTrainings(WorkoutsService workoutsService) {
        this.workoutsService = workoutsService;
    }

    @Scheduled(cron = "0 0 0 ? * 7") // Runs at midnight at the end of Saturday
    @Transactional
    @CacheEvict(value = "weeklyTrainings", allEntries = true)
    public void createAndDeleteWeeklyTrainings() {
        // Populate new weekly trainings
        workoutsService.populateWeeklyTrainings();

    }
}
