package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.service.UserActivationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActivationLinkCleanupScheduler {

    private UserActivationService activationService;

    public ActivationLinkCleanupScheduler(UserActivationService activationService) {
        this.activationService = activationService;
    }

    @Scheduled(cron = "0 0 0 * * 7")
    public void cleanUp() {
        // Trigger cleanup every week at midnight on Sunday (beginning of Sunday)
        activationService.cleanUpActivationLinks();
    }
}
