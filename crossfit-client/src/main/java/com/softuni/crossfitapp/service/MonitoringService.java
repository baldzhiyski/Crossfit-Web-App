package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.entity.enums.TrainingType;

public interface MonitoringService {
    void increaseBlogReaders();
    void enrollUserInTraining(String trainingType);
}
