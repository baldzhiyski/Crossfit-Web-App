package com.softuni.crossfitapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<com.softuni.crossfitapp.model.entity.Training, UUID> {
}
