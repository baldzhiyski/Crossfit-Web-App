package com.softuni.crossfitapp.domain.dto.users;

import com.softuni.crossfitapp.domain.dto.roles.SeedRoleDto;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class SeedAdminDto {private String username;

    private String firstName;

    private String lastName;

    private String address;
    private boolean isActive;

    private String nationality;

    private String town;

    private Date bornOn;

    private String password;

    private String imageUrl;

    private String telephoneNumber;

    private String email;

    private Set<SeedRoleDto> roles;

    private String membershipStartDate;

    private String membershipEndDate;
}
