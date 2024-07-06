package com.softuni.crossfitapp.domain.dto.memberships;

import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class MembershipProfilePageDto {

    private MembershipType membershipType;

    private Long price;

    private Long duration;

    private Long timeLeft;
}
