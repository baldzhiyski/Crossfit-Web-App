package com.softuni.crossfitapp.domain.dto.users;

import com.softuni.crossfitapp.domain.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SeedAdminDto {private String username;

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

    private String membershipStartDate;

    private String membershipEndDate;
}
