package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeeklyTrainingRepository extends JpaRepository<WeeklyTraining, Long> {

    Optional<WeeklyTraining> findByUuid(UUID uuid);

    List<WeeklyTraining> findAllByDayOfWeek(DayOfWeek dayOfWeek);

    @Modifying
    @Transactional
    @Query("DELETE FROM WeeklyTraining wt")
    void deleteAllWeeklyTrainings();
}
