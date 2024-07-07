package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.trainings.SeedTrainingFromApiDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;

import java.util.Set;

public interface WorkoutsService {

    // TODO : Declare some methods for taking diff types of trainings from our rest api

    Set<SeedTrainingFromApiDto> getAllTrainings();

    void seedTrainings();

    TrainingDetailsDto getTrainingDto(TrainingType trainingType);
}
