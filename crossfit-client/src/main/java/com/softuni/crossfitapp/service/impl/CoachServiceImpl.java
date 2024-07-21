package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.coaches.CoachDisplayDto;
import com.softuni.crossfitapp.domain.dto.trainings.WeeklyTrainingDto;
import com.softuni.crossfitapp.domain.entity.*;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.events.CancelledTrainingEvent;
import com.softuni.crossfitapp.exceptions.AccessOnlyForCoaches;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.CoachRepository;
import com.softuni.crossfitapp.repository.RoleRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.repository.WeeklyTrainingRepository;
import com.softuni.crossfitapp.service.CoachService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CoachServiceImpl implements CoachService {

    private ApplicationEventPublisher applicationEventPublisher;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private CoachRepository coachRepository;

    private WeeklyTrainingRepository weeklyTrainingRepository;

    private ModelMapper mapper;

    public CoachServiceImpl(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepository, RoleRepository roleRepository, CoachRepository coachRepository, WeeklyTrainingRepository weeklyTrainingRepository, ModelMapper mapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.coachRepository = coachRepository;
        this.weeklyTrainingRepository = weeklyTrainingRepository;
        this.mapper = mapper;
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

    @Override
    @Transactional
    public void closeTrainingSession(String coachUsername, UUID weeklyTrainingId) {
        Optional<Coach> byUsername = this.coachRepository.findByUsername(coachUsername);
        WeeklyTraining weeklyTraining = this.weeklyTrainingRepository.findByUuid(weeklyTrainingId).orElseThrow(() -> new ObjectNotFoundException("Ïnvalid weekly training id!"));



        if(byUsername.isEmpty()){
            throw  new AccessOnlyForCoaches("This functionality is only for coaches available !");
        }
        List<User> participants = weeklyTraining.getParticipants();
        for (User participant : participants) {
            participant.getTrainingsPerWeekList().remove(weeklyTraining);
            this.userRepository.save(participant);


            applicationEventPublisher.publishEvent(new CancelledTrainingEvent(
                    "CoachService",
                    byUsername.get().getFirstName() + ' ' + byUsername.get().getLastName(),
                    weeklyTrainingId,
                    participant.getEmail(),
                    participant.getFullName()
                    ));

        }
        byUsername.get().getTrainingsPerWeek().remove(weeklyTraining);
        weeklyTraining.getParticipants().clear();
        this.weeklyTrainingRepository.delete(weeklyTraining);
        this.coachRepository.save(byUsername.get());
    }

    @Override
    public List<WeeklyTrainingDto> getUpcomingTrainings(String usernameOfCoach) {

        Optional<Coach> byUsername = this.coachRepository.findByUsername(usernameOfCoach);

        if(byUsername.isEmpty()){
            throw  new AccessOnlyForCoaches("This functionality is only for coaches available !");
        }

        return byUsername.get().getTrainingsPerWeek()
                .stream()
                .map(weeklyTraining -> this.mapper.map(weeklyTraining,WeeklyTrainingDto.class))
                .collect(Collectors.toList());
    }

}
