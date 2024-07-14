package com.softuni.crossfitapp.domain.dto.trainings;

import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeedTrainingFromApiDto {
    private String description;

    private Level level;
    private TrainingType trainingType;



}
