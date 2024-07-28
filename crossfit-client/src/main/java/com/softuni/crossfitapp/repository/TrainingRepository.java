package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<com.softuni.crossfitapp.domain.entity.Training, Long> {
    Optional<Training> findByTrainingType(TrainingType trainingType);
    Optional<Training> findByUuid(UUID trainingUUid);

    @Query("SELECT t FROM Training t WHERE t.trainingType = :trainingType ORDER BY RAND()")
    Training findRandomTrainingByTrainingType(@Param("trainingType") TrainingType trainingType);
}
