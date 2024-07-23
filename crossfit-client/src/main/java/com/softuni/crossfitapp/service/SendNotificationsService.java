package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.events.CancelledTrainingEvent;
import com.softuni.crossfitapp.domain.events.DisabledAccountEvent;
import com.softuni.crossfitapp.domain.events.EnabledAccountEvent;
import com.softuni.crossfitapp.domain.events.UserRegisteredEvent;

public interface SendNotificationsService {

    void cancelTraining(CancelledTrainingEvent cancelledTrainingEvent);

    void disableAccount(DisabledAccountEvent disabledAccountEvent);

    void enableAccount(EnabledAccountEvent enabledAccountEvent);

    void userRegistered(UserRegisteredEvent event);

}
