package com.softuni.crossfitapp.service;

import java.util.List;

public interface CountryService {

    void seedCountries();

    List<String> allCountryCodes();
}
