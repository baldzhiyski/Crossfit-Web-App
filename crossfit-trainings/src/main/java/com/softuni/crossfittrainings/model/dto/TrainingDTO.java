package com.softuni.crossfittrainings.model.dto;

import com.softuni.crossfittrainings.model.enums.Level;
import com.softuni.crossfittrainings.model.enums.TrainingType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TrainingDTO {


    @Column
    @NotBlank
    @Size(min = 10,max = 200)
    private String description;


    @Enumerated(EnumType.STRING)
    @NotNull
    private Level level;

    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;
}
