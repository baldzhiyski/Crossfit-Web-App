package com.softuni.crossfittrainings.repository;

import com.softuni.crossfittrainings.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<Training, UUID> {
}
