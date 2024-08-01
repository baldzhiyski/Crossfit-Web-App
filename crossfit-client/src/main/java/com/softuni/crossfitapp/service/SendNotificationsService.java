package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.events.*;

public interface SendNotificationsService {

    void cancelTraining(CancelledTrainingEvent cancelledTrainingEvent);

    void disableAccount(DisabledAccountEvent disabledAccountEvent);

    void enableAccount(EnabledAccountEvent enabledAccountEvent);

    void userRegistered(UserRegisteredEvent event);

    void notifyUserLoggedWithGitHubWithoutRegistration(LoggedViaGitHubEvent loggedViaGitHubEvent);
}
