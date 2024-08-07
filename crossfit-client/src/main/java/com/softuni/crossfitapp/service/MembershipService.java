package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.exrates.ExRatesDTO;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDtoWrapper;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;

import java.util.List;

public interface MembershipService {

    List<MembershipDto> getMemberships();

    MembershipDtoWrapper getWrapper();

    MembershipDto getMembershipDto(MembershipType membershipType);
}
