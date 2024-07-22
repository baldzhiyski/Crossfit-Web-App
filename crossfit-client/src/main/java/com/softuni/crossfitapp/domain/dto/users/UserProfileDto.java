package com.softuni.crossfitapp.domain.dto.users;


import com.softuni.crossfitapp.domain.dto.events.EventDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipProfilePageDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDto;
import com.softuni.crossfitapp.domain.entity.Membership;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;

import java.util.Set;
import java.util.UUID;

public record UserProfileDto(String fullName,
                             String username,

                             String imageUrl,
                             UUID uuid,
                             String email,
                             String address,
                             Set<RoleType> roles,
                             MembershipProfilePageDto membership
                             ,
                             Set<TrainingDto> enrolledTrainingsForTheWeek)
{
}
