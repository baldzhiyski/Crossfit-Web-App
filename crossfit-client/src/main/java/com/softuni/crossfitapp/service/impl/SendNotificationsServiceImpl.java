package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.UserActivationLinkEntity;
import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import com.softuni.crossfitapp.domain.events.*;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.UserActivationCodeRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.repository.WeeklyTrainingRepository;
import com.softuni.crossfitapp.service.EmailService;
import com.softuni.crossfitapp.service.SendNotificationsService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

@Service
public class SendNotificationsServiceImpl implements SendNotificationsService {
    private static final String ACTIVATION_CODE_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";
    private static final int ACTIVATION_CODE_LENGTH = 20;

    private UserRepository userRepository;

    private UserActivationCodeRepository userActivationCodeRepository;

    private WeeklyTrainingRepository weeklyTrainingRepository;
    private EmailService emailService;

    public SendNotificationsServiceImpl(UserRepository userRepository, UserActivationCodeRepository userActivationCodeRepository, WeeklyTrainingRepository weeklyTrainingRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
        this.weeklyTrainingRepository = weeklyTrainingRepository;
        this.emailService = emailService;
    }

    @Override
    @EventListener(CancelledTrainingEvent.class)
    public void cancelTraining(CancelledTrainingEvent cancelledTrainingEvent) {
        WeeklyTraining weeklyTraining = this.weeklyTrainingRepository.findByUuid(cancelledTrainingEvent.getWeeklyTrainingId()).orElseThrow(() -> new ObjectNotFoundException("No such weekly training !"));
        emailService.sendCancelledTrainingEmail(cancelledTrainingEvent.getCoachFullName(),weeklyTraining,cancelledTrainingEvent.getUserEmail(),cancelledTrainingEvent.getUserFullName() );
    }

    @Override
    @EventListener(DisabledAccountEvent.class)
    public void disableAccount(DisabledAccountEvent disabledAccountEvent) {
        this.emailService.sendDisabledAccountEmail(disabledAccountEvent.getUuid(),disabledAccountEvent.getUsername(),disabledAccountEvent.getFullName(),disabledAccountEvent.getEmail());

    }

    @Override
    @EventListener(EnabledAccountEvent.class)
    public void enableAccount(EnabledAccountEvent enabledAccountEvent) {
        this.emailService.sendEnabledAccount(enabledAccountEvent.getUuid(),enabledAccountEvent.getUsername(),enabledAccountEvent.getFullName(),enabledAccountEvent.getEmail());
    }

    @Override
    @EventListener(UserRegisteredEvent.class)
    public void userRegistered(UserRegisteredEvent event) {
        String activationCode = activationCode(event.getUserEmail());

        User savedUser = this.userRepository.findByEmail(event.getUserEmail()).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        emailService.sendRegistrationEmail(event.getUserEmail(),
                event.getUserNames(),
                activationCode,savedUser);
    }

    @Override
    @EventListener(LoggedViaGitHubEvent.class)
    public void notifyUserLoggedWithGitHubWithoutRegistration(LoggedViaGitHubEvent loggedViaGitHubEvent) {
        emailService.sendUserLoggedViaGitHubEmailWithRawRandomPass(loggedViaGitHubEvent.getUuid(),
                loggedViaGitHubEvent.getUsername(),
                loggedViaGitHubEvent.getFullName(),
                loggedViaGitHubEvent.getEmail(),
                loggedViaGitHubEvent.getCurrentPassword(),
                loggedViaGitHubEvent.getAddress(),
                loggedViaGitHubEvent.getDateOfBirth());
    }


    public String activationCode(String email) {
        String activationCode = generateActivationCode();

        UserActivationLinkEntity userActivationCodeEntity = new UserActivationLinkEntity();
        userActivationCodeEntity.setActivationCode(activationCode);
        userActivationCodeEntity.setCreated(Instant.now());
        userActivationCodeEntity.setUserEntity(userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User not found")));

        userActivationCodeRepository.saveAndFlush(userActivationCodeEntity);

        return activationCode;

    }

    private static String generateActivationCode() {

        StringBuilder activationCode = new StringBuilder();
        Random random = new SecureRandom();

        for (int i = 0; i < ACTIVATION_CODE_LENGTH; i++) {
            int randInx = random.nextInt(ACTIVATION_CODE_SYMBOLS.length());
            activationCode.append(ACTIVATION_CODE_SYMBOLS.charAt(randInx));
        }

        return activationCode.toString();


    }
}
