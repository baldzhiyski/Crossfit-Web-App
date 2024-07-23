package com.softuni.crossfitapp.domain.dto.comments;


import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;


@Getter
@Setter
public class CommentAdminPageDto {

    private UUID uuid;

    private String text;

    private String authorUsername;

    private TrainingType trainingTrainingType;

}
