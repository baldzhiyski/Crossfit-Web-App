package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeeklyTrainingRepository extends JpaRepository<WeeklyTraining, Long> {

    Optional<WeeklyTraining> findByUuid(UUID uuid);

    List<WeeklyTraining> findAllByDayOfWeek(DayOfWeek dayOfWeek);
}
