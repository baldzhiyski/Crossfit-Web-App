package com.softuni.crossfitapp.service;


import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.comments.DisplayCommentDto;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;

import java.util.List;

public interface CommentService {

    void addComment(AddCommentDto addCommentDto, TrainingType trainingType);

    List<DisplayCommentDto> getAllCommentsOnCurrentTrainingType(TrainingType trainingType);

    void cleanUpOldComments();
}
