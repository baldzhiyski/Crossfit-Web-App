package com.softuni.crossfitapp.domain.dto.trainings;

import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingDto {

    private TrainingType trainingType;
}
