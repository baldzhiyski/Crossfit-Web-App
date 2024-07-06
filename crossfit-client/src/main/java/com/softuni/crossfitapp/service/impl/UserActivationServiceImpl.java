package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.UserActivationLinkEntity;
import com.softuni.crossfitapp.domain.events.UserRegisteredEvent;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.UserActivationCodeRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.EmailService;
import com.softuni.crossfitapp.service.UserActivationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

@Service
public class UserActivationServiceImpl implements UserActivationService {

    private static final String ACTIVATION_CODE_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";
    private static final int ACTIVATION_CODE_LENGTH = 20;
    private EmailService emailService;


    private final UserRepository userRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;

    public UserActivationServiceImpl(EmailService emailService, UserRepository userRepository, UserActivationCodeRepository userActivationCodeRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
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
    public void cleanUpActivationLinks() {
        this.userActivationCodeRepository.deleteAll();
    }

    @Override
    public String activationCode(String email) {
        String activationCode = generateActivationCode();

        UserActivationLinkEntity userActivationCodeEntity = new UserActivationLinkEntity();
        userActivationCodeEntity.setActivationCode(activationCode);
        userActivationCodeEntity.setCreated(Instant.now());
        userActivationCodeEntity.setUser(userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User not found")));

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
