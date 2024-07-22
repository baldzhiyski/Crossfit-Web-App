package com.softuni.crossfitapp.domain.dto.trainings;

import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TrainingDto {

    private TrainingType trainingType;
    private LocalDate date;
}
