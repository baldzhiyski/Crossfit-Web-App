package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.coaches.CoachDisplayDto;
import com.softuni.crossfitapp.domain.entity.Certificate;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.repository.CoachRepository;
import com.softuni.crossfitapp.repository.RoleRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.CoachService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CoachServiceImpl implements CoachService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private CoachRepository coachRepository;

    public CoachServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CoachRepository coachRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.coachRepository = coachRepository;
    }

    @Override
    @Cacheable("display-coaches")
    public List<CoachDisplayDto> getCoachesInfoForMeetTheTeamPage() {
        Role coachRole = this.roleRepository.findByRoleType(RoleType.COACH);
        Set<User> coachesAccounts = this.userRepository.findByRolesContaining(coachRole);

         return coachesAccounts.stream().map(coach->{
            List<String> certificatesNames = this.coachRepository
                    .findByFirstName(coach.getFirstName()).getCertificates().stream().map(Certificate::getName).toList();
            return new CoachDisplayDto(coach.getFirstName(),coach.getLastName(),certificatesNames,coach.getImageUrl(),coach.getEmail());
        }).toList();
    }
}
