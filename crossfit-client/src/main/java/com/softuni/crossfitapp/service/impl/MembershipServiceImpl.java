package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.exrates.ExRatesDTO;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDtoWrapper;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.MembershipRepository;
import com.softuni.crossfitapp.service.ExchangeRateService;
import com.softuni.crossfitapp.service.MembershipService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipServiceImpl implements MembershipService {
    private MembershipRepository membershipRepository;

    public MembershipServiceImpl(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Cacheable("memberships")
    @Override
    public List<MembershipDto> getMemberships() {
        return membershipRepository.findAll()
                .stream()
                .map(MembershipDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MembershipDtoWrapper getWrapper() {
        MembershipDtoWrapper membershipDtoWrapper = new MembershipDtoWrapper();
        membershipDtoWrapper.setMembershipDtos(getMemberships());
        return membershipDtoWrapper;
    }

    @Override
    public MembershipDto getMembershipDto(MembershipType membershipType) {
        return  MembershipDto.fromEntity(this.membershipRepository.findByMembershipType(membershipType).orElseThrow(()->new ObjectNotFoundException("No such membership !")));
    }
}
