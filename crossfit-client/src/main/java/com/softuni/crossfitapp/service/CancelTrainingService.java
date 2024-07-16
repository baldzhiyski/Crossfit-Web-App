package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.events.CancelledTrainingEvent;

import java.util.UUID;

public interface CancelTrainingService {

    void cancelTraining(CancelledTrainingEvent cancelledTrainingEvent);
}
