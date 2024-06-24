package com.softuni.crossfitapp.domain.dto.certificates;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CertificateDto {

    private String name;
    private Date obtainedOn;
}
