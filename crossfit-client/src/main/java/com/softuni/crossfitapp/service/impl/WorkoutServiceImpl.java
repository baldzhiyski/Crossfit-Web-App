package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.config.rest.WorkoutsAPIConfig;
import com.softuni.crossfitapp.domain.dto.trainings.SeedTrainingFromApiDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.TrainingRepository;
import com.softuni.crossfitapp.service.WorkoutsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Set;

@Service
public class WorkoutServiceImpl implements WorkoutsService {
    private final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);
    private TrainingRepository trainingRepository;

    private RestClient trainingsRestClient;

    private WorkoutsAPIConfig workoutsAPIConfig;

    private ModelMapper mapper;

    public WorkoutServiceImpl(TrainingRepository trainingRepository, @Qualifier("trainingsRestClient") RestClient trainingsRestClient, WorkoutsAPIConfig workoutsAPIConfig, ModelMapper mapper) {
        this.trainingRepository = trainingRepository;
        this.trainingsRestClient = trainingsRestClient;
        this.workoutsAPIConfig = workoutsAPIConfig;
        this.mapper = mapper;
    }

    public boolean hasInitializedWorkouts() {
        return this.trainingRepository.count() > 0;
    }


    @Override
    public Set<SeedTrainingFromApiDto> getAllTrainings() {
        LOGGER.info("Getting the trainings from the db ....");
        Set<SeedTrainingFromApiDto> body = trainingsRestClient
                .get()
                .uri(workoutsAPIConfig.getAllTrainingsUrl())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return body;
    }

    @Override
    public void seedTrainings() {
        LOGGER.info("Seeding is beginning....");

        if(!hasInitializedWorkouts()){
            getAllTrainings().stream().map(seedTrainingFromApiDto -> {
                Training mapped = this.mapper.map(seedTrainingFromApiDto, Training.class);
                setSpecificPictureUrl(mapped);
                return mapped;
            }).forEach(this.trainingRepository::save);
        }

    }

    @Override
    public TrainingDetailsDto getTrainingDto(TrainingType trainingType) {
        Training training = this.trainingRepository
                .findByTrainingType(trainingType)
                .orElseThrow(() -> new ObjectNotFoundException("No such training specified in the crossfit community !"));

        return mapper.map(training, TrainingDetailsDto.class);
    }

    private void setSpecificPictureUrl(Training mapped) {
        switch (mapped.getTrainingType()){
            case WEIGHTLIFTING -> mapped.setImageUrl("/images/weightlifting.jpg");
            case WOD ->  mapped.setImageUrl("/images/wod.jpg");
            case HYROX ->  mapped.setImageUrl("/images/hyrox.webp");
            case CARDIO -> mapped.setImageUrl("/images/cardio.webp");
            case MUMFIT -> mapped.setImageUrl("/images/mumfit.jpg");
            case OPENGYM -> mapped.setImageUrl("/images/crossfit.jpg");
            case GYMNASTICS -> mapped.setImageUrl("/images/gymnastics.webp");
            default -> mapped.setImageUrl("/images/weights.jpg");
        }
    }
}
