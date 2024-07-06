package com.softuni.crossfitapp.domain.dto.users;

import com.softuni.crossfitapp.domain.dto.memberships.SeedMembershipTypeDto;
import com.softuni.crossfitapp.domain.dto.roles.SeedRoleDto;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class SeedCoachesUserProfileDto {
    private String username;

    private String firstName;

    private String lastName;

    private String address;

    private String nationality;

    private boolean isActive;

    private String town;

    private Date bornOn;

    private String password;

    private String imageUrl;

    private String telephoneNumber;

    private String email;

    private Set<SeedRoleDto> roles;

    private SeedMembershipTypeDto membership;

    private String membershipStartDate;

    private String membershipEndDate;
}
