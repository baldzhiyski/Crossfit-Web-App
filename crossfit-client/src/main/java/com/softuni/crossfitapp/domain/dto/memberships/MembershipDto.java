package com.softuni.crossfitapp.domain.dto.memberships;


import com.softuni.crossfitapp.domain.entity.Membership;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;


public record MembershipDto(MembershipType membershipType,
                            Long price,
                            Long duration) {

    public static MembershipDto fromEntity(Membership membership){
        return new MembershipDto(membership.getMembershipType(),
                membership.getPrice(),
                membership.getDuration());
    }
}
