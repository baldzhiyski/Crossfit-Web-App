package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.coaches.CoachDisplayDto;

import java.util.List;

public interface CoachService {

    List<CoachDisplayDto> getCoachesInfoForMeetTheTeamPage();
}
