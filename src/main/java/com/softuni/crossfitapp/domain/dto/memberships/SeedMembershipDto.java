package com.softuni.crossfitapp.domain.dto.memberships;

import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeedMembershipDto {
    private MembershipType membershipType;
    private Long price;
    private Integer duration;
}
