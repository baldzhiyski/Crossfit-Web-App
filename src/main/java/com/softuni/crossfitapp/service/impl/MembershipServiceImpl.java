package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.exrates.ExRatesDTO;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDtoWrapper;
import com.softuni.crossfitapp.repository.MembershipRepository;
import com.softuni.crossfitapp.service.ExchangeRateService;
import com.softuni.crossfitapp.service.MembershipService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipServiceImpl implements MembershipService {
    private MembershipRepository membershipRepository;

    public MembershipServiceImpl(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Override
    public List<MembershipDto> getMemberships() {
        return membershipRepository.findAll()
                .stream()
                .map(MembershipDto::fromEntity)
                .toList();
    }

    @Override
    public MembershipDtoWrapper getWrapper() {
        MembershipDtoWrapper membershipDtoWrapper = new MembershipDtoWrapper();
        membershipDtoWrapper.setMembershipDtos(getMemberships());
        return membershipDtoWrapper;
    }
}
