package com.softuni.crossfitapp.domain.dto.coaches;

import com.softuni.crossfitapp.domain.dto.certificates.CertificateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SeedCoachDto {
    private String firstName;
    private String lastName;

    private List<CertificateDto> certificates;
}
