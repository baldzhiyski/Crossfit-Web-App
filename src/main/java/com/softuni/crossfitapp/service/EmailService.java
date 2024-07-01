package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.entity.User;

public interface EmailService {
    void sendRegistrationEmail(
            String userEmail,
            String userName,
            String activationCode, User savedUser);
}
