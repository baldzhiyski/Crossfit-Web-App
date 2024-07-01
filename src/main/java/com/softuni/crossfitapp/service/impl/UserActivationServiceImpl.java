package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.events.UserRegisteredEvent;
import com.softuni.crossfitapp.service.EmailService;
import com.softuni.crossfitapp.service.UserActivationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserActivationServiceImpl implements UserActivationService {
    private EmailService emailService;

    public UserActivationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    @EventListener(UserRegisteredEvent.class)
    public void userRegistered(UserRegisteredEvent event) {
        // TODO : Add activation links

        emailService.sendRegistrationEmail(event.getUserEmail(), event.getUserNames());
    }
}
