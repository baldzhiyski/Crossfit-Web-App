package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.coaches.CoachDisplayDto;
import com.softuni.crossfitapp.domain.entity.Certificate;
import com.softuni.crossfitapp.domain.entity.Coach;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.repository.CoachRepository;
import com.softuni.crossfitapp.repository.RoleRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.repository.WeeklyTrainingRepository;
import com.softuni.crossfitapp.service.CoachService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoachServiceImplTest {
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private WeeklyTrainingRepository weeklyTrainingRepository;

    @Mock
    private ModelMapper mapper;


    private CoachService coachService;

    private  List<CoachDisplayDto> coachDisplayDtos;

    @BeforeEach
    public void setUp(){
        this.coachService = new CoachServiceImpl( applicationEventPublisher, userRepository,  roleRepository,  coachRepository, weeklyTrainingRepository,mapper);
        coachDisplayDtos = getSomeCoachesDtos();
    }

    @Test
    public void testGetCoachesInfoForMeetTheTeamPage() {
        // Arrange
        Role coachRole = new Role();
        coachRole.setRoleType(RoleType.COACH);

        User coach1 = new User();
        coach1.setFirstName("John");
        coach1.setLastName("Doe");
        coach1.setImageUrl("john.jpg");
        coach1.setEmail("john@example.com");

        User coach2 = new User();
        coach2.setFirstName("Jane");
        coach2.setLastName("Smith");
        coach2.setImageUrl("jane.jpg");
        coach2.setEmail("jane@example.com");

        Set<User> coachesAccounts = Set.of(coach1,coach2);

        when(roleRepository.findByRoleType(RoleType.COACH)).thenReturn(coachRole);
        when(userRepository.findByRolesContaining(coachRole)).thenReturn(coachesAccounts);

        Certificate cert1 = new Certificate();
        cert1.setName("Cert1");

        Certificate cert2 = new Certificate();
        cert2.setName("Cert2");

        when(coachRepository.findByFirstName("John")).thenReturn(Coach.builder().firstName("John").certificates(List.of(cert1)).build());
        when(coachRepository.findByFirstName("Jane")).thenReturn(Coach.builder().firstName("Jane").certificates(List.of(cert2)).build());
        // Act
        List<CoachDisplayDto> coachesInfo = coachService.getCoachesInfoForMeetTheTeamPage();

        // Assert
        assertEquals(2, coachesInfo.size());
    }


    private List<CoachDisplayDto> getSomeCoachesDtos() {
        List<CoachDisplayDto> list = new ArrayList<>();
        CoachDisplayDto coachDisplayDtoFirst = new CoachDisplayDto("Petar","Petrov",List.of("Crossfit Level 1"),"/images/petar,jpeg","petar@abg.bg");
        CoachDisplayDto coachDisplayDtoSecond= new CoachDisplayDto("Simon","Simonov",List.of("Crossfit Level 2"),"/images/simon,jpeg","simon@abg.bg");
        CoachDisplayDto coachDisplayDtoThird = new CoachDisplayDto("Matei","Mateev",List.of("Crossfit Level 3"),"/images/matei,jpeg","matei@abg.bg");


        list.add(coachDisplayDtoFirst);
        list.add(coachDisplayDtoSecond);
        list.add(coachDisplayDtoThird);

        return list;
    }

}