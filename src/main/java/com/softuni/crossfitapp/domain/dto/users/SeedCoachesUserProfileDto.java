package com.softuni.crossfitapp.domain.dto.users;

import com.softuni.crossfitapp.domain.dto.memberships.MembershipDto;
import com.softuni.crossfitapp.domain.dto.memberships.SeedMembershipDto;
import com.softuni.crossfitapp.domain.entity.Membership;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import com.softuni.crossfitapp.vallidation.annotations.ValidEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SeedCoachesUserProfileDto {
    private String username;

    private String firstName;

    private String lastName;

    private String address;

    private String nationality;

    private String town;

    private Date bornOn;

    private String password;

    private String imageUrl;

    private String telephoneNumber;

    private String email;

    private Role role;

    private MembershipDto membership;

    private String membershipStartDate;

    private String membershipEndDate;
}
