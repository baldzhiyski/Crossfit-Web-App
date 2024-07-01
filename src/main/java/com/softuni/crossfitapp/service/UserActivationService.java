package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.events.UserRegisteredEvent;

public interface UserActivationService {

    void userRegistered(UserRegisteredEvent event);
}
