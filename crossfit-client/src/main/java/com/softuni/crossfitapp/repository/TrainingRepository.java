package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<com.softuni.crossfitapp.domain.entity.Training, UUID> {
    Optional<Training> findByTrainingType(TrainingType trainingType);
}