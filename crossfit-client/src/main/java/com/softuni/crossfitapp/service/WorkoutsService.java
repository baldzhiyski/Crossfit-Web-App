package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.trainings.SeedTrainingFromApiDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.dto.trainings.WeeklyTrainingDto;
import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface WorkoutsService {
    Set<SeedTrainingFromApiDto> getAllTrainings();

    void seedTrainings();

    TrainingDetailsDto getTrainingDto(TrainingType trainingType);
    void populateWeeklyTrainings();

    List<WeeklyTrainingDto> getWeeklyTrainingsSpecificDay(DayOfWeek dayOfWeek);

    void joinCurrentTraining(UUID trainingId, String loggedUserUsername);

    List<WeeklyTrainingDto> getCurrentWeeklyTrainings(String loggedUserUsername);

    void deleteFromCurrentTrainings(UUID trainingId, String loggedUserUsername);


    List<WeeklyTraining> getTrainingsWithDateBefore(LocalDate currentDate, LocalTime localTime);

    void doDeletion(List<WeeklyTraining> pastTrainings);

    TrainingType getTrainingTypeById(UUID trainingId);
}
