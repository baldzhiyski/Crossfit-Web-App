package com.softuni.crossfitapp.domain.dto.coaches;

import com.softuni.crossfitapp.domain.dto.certificates.SeedCertificateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SeedCoachDto {
    private String username;
    private String firstName;
    private String lastName;

    private List<SeedCertificateDto> certificates;
}
