package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.WeeklyTraining;

import java.util.UUID;

public interface EmailService {
    void sendRegistrationEmail(
            String userEmail,
            String userName,
            String activationCode, User savedUser);


    void sendCancelledTrainingEmail(
            String coachFullName,
            WeeklyTraining weeklyTraining,
            String userEmail,
            String userFullName);

    void sendDisabledAccountEmail(UUID authorUUID, String username, String fullName, String email);

    void sendEnabledAccount(UUID authorUUID, String username, String fullName, String email);
}
