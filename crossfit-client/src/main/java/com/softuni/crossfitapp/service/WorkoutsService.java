package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.trainings.SeedTrainingFromApiDto;

import java.util.Set;

public interface WorkoutsService {

    // TODO : Declare some methods for taking diff types of trainings from our rest api

    Set<SeedTrainingFromApiDto> getAllTrainings();

    void seedTrainings();
}
