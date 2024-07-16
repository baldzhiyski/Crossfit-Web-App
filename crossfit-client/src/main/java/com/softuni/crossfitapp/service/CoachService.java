package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.coaches.CoachDisplayDto;
import com.softuni.crossfitapp.domain.entity.Coach;
import com.softuni.crossfitapp.domain.entity.WeeklyTraining;

import java.util.List;
import java.util.UUID;

public interface CoachService {

    List<CoachDisplayDto> getCoachesInfoForMeetTheTeamPage();

    void closeTrainingSession(String coachUsername, UUID weeklyTrainingId);

}
