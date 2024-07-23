package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.repository.UserActivationCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActivationLinkCleanupScheduler {

   private UserActivationCodeRepository activationCodeRepository;

    @Scheduled(cron = "0 0 0 * * 7")
    public void cleanUp() {
        // Trigger cleanup every week at midnight on Sunday (beginning of Sunday)
       activationCodeRepository.deleteAll();
    }
}
