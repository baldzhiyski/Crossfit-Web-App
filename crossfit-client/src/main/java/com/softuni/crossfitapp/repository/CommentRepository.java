package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Comment;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByTraining_TrainingType(TrainingType trainingType);
}
