package com.softuni.crossfittrainings.service;

import com.softuni.crossfittrainings.model.Training;
import com.softuni.crossfittrainings.model.dto.TrainingDTO;

import java.util.Set;
import java.util.UUID;

public interface TrainingService {
    TrainingDTO getTrainingById(UUID id);

    Set<TrainingDTO> getAllTrainings();

    void deleteOffer(UUID id);
}
