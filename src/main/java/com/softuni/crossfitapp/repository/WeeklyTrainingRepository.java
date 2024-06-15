package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.model.entity.WeeklyTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WeeklyTrainingRepository extends JpaRepository<WeeklyTraining, UUID> {
}
