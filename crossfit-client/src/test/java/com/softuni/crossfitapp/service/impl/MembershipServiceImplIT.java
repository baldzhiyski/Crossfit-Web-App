package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.memberships.MembershipDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDtoWrapper;
import com.softuni.crossfitapp.domain.entity.Membership;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.MembershipRepository;
import com.softuni.crossfitapp.service.MembershipService;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MembershipServiceImplIT {


    private MembershipService membershipService;
    @Autowired
    private MembershipRepository membershipRepository;


    @Autowired
    private TestData testData;

    @Autowired
    private ModelMapper mapper;


    @BeforeEach
    public void setUp(){
        this.membershipService= new MembershipServiceImpl(membershipRepository);
        createMembership();
    }

    @AfterEach
    public void tearDown(){
        this.testData.deleteMemberships();
    }

    @Test
    public void testGetAllMemberships(){
        List<MembershipDto> allMemberships = this.membershipService.getMemberships();
        Assertions.assertEquals(3,allMemberships.size());
        Assertions.assertTrue(allMemberships.stream().anyMatch(membershipDto -> membershipDto.membershipType().equals(MembershipType.ELITE)));
        Assertions.assertTrue(allMemberships.stream().anyMatch(membershipDto -> membershipDto.membershipType().equals(MembershipType.BASIC)));
        Assertions.assertTrue(allMemberships.stream().anyMatch(membershipDto -> membershipDto.membershipType().equals(MembershipType.UNLIMITED)));
        Assertions.assertFalse(allMemberships.stream().anyMatch(membershipDto -> membershipDto.membershipType().equals(MembershipType.PREMIUM)));
    }

    @Test
    public void testGetAllMembershipsWrapper(){
        MembershipDtoWrapper allMemberships = this.membershipService.getWrapper();
        Assertions.assertEquals(3,allMemberships.getMembershipDtos().size());
        Assertions.assertTrue(allMemberships.getMembershipDtos().stream().anyMatch(membershipDto -> membershipDto.membershipType().equals(MembershipType.ELITE)));
        Assertions.assertTrue(allMemberships.getMembershipDtos().stream().anyMatch(membershipDto -> membershipDto.membershipType().equals(MembershipType.BASIC)));
        Assertions.assertTrue(allMemberships.getMembershipDtos().stream().anyMatch(membershipDto -> membershipDto.membershipType().equals(MembershipType.UNLIMITED)));
        Assertions.assertFalse(allMemberships.getMembershipDtos().stream().anyMatch(membershipDto -> membershipDto.membershipType().equals(MembershipType.PREMIUM)));
    }

    @Test
    public void testGetDtoFromType(){
        MembershipDto membershipDto = this.membershipService.getMembershipDto(MembershipType.ELITE);
        Assertions.assertNotNull(membershipDto);

        Assertions.assertThrows(ObjectNotFoundException.class,()->{
                this.membershipService.getMembershipDto(MembershipType.PREMIUM);
        });
    }



    private void createMembership() {
        this.membershipRepository.saveAndFlush(Membership.builder().membershipType(MembershipType.ELITE).duration(3L).price(200L).build());
        this.membershipRepository.saveAndFlush(Membership.builder().membershipType(MembershipType.BASIC).duration(1L).price(100L).build());
        this.membershipRepository.saveAndFlush(Membership.builder().membershipType(MembershipType.UNLIMITED).duration(6L).price(400L).build());
    }


}
