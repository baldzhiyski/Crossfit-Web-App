package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.WeeklyTraining;

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
}
