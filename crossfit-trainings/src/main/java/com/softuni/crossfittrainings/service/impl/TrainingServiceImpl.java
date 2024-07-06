package com.softuni.crossfittrainings.service.impl;

import com.softuni.crossfittrainings.model.Training;
import com.softuni.crossfittrainings.model.dto.TrainingDTO;
import com.softuni.crossfittrainings.repository.TrainingRepository;
import com.softuni.crossfittrainings.service.TrainingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public TrainingDTO getTrainingById(UUID id) {

        return mapToTrainingDto(this.trainingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Training not found in the database!")));
    }

    @Override
    public Set<TrainingDTO> getAllTrainings() {
        return this.trainingRepository.findAll()
                .stream()
                .map(this::mapToTrainingDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteOffer(UUID id) {
        Training training = this.trainingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Training not found in the database!"));
        this.trainingRepository.delete(training);
    }

    private TrainingDTO mapToTrainingDto(Training training) {
        return  TrainingDTO.builder().description(training.getDescription())
                .trainingType(training.getTrainingType())
                .level(training.getLevel())
                .build();
    }
}
