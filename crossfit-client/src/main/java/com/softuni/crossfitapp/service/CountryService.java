package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.countries.CountryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CountryService {

    void seedCountries();

    List<String> allCountryCodes();

    CountryDto[] fetchCountries();

    @Transactional
    void updateCountries(CountryDto[] countryDtos);
}
