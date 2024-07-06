package com.softuni.crossfitapp.domain.dto.trainings;

import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SeedTrainingFromApiDto {
    private String description;

    private Level level;
    private TrainingType trainingType;



}
