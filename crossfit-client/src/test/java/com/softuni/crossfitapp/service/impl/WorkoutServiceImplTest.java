package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.config.rest.WorkoutsAPIConfig;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.CoachRepository;
import com.softuni.crossfitapp.repository.TrainingRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.repository.WeeklyTrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private WeeklyTrainingRepository weeklyTrainingRepository;

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestClient trainingsRestClient;

    @Mock
    private WorkoutsAPIConfig workoutsAPIConfig;

    @Mock
    private ModelMapper mapper;

    private WorkoutServiceImpl workoutService;

    @Captor
    private ArgumentCaptor<Training> trainingCaptor;

    @BeforeEach
    void setUp() {
       workoutService = new WorkoutServiceImpl( trainingRepository,  weeklyTrainingRepository,  coachRepository,  userRepository,
                trainingsRestClient,  workoutsAPIConfig,  mapper);
    }

    @Test
    void testHasInitializedWorkouts() {
        when(trainingRepository.count()).thenReturn(1L);
        assertThat(workoutService.hasInitializedWorkouts()).isTrue();

        when(trainingRepository.count()).thenReturn(0L);
        assertThat(workoutService.hasInitializedWorkouts()).isFalse();
    }

    @Test
    void testGetTrainingDto() {
        TrainingType trainingType = TrainingType.CARDIO;
        Training training = new Training();
        training.setTrainingType(trainingType);
        training.setDescription("Cardio Training");

        when(trainingRepository.findByTrainingType(trainingType)).thenReturn(Optional.of(training));

        TrainingDetailsDto trainingDetailsDto = new TrainingDetailsDto();
        trainingDetailsDto.setDescription("Cardio Training");
        when(mapper.map(training, TrainingDetailsDto.class)).thenReturn(trainingDetailsDto);

        TrainingDetailsDto result = workoutService.getTrainingDto(trainingType);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Cardio Training");
    }

    @Test
    void testGetTrainingDtoNotFound() {
        TrainingType trainingType = TrainingType.CARDIO;

        assertThrows(ObjectNotFoundException.class, () -> workoutService.getTrainingDto(trainingType));
    }
}