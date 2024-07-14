package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.config.rest.CountriesNowConfig;
import com.softuni.crossfitapp.domain.dto.countries.CountryDto;
import com.softuni.crossfitapp.domain.entity.Country;
import com.softuni.crossfitapp.repository.CountryRepository;
import com.softuni.crossfitapp.service.CountryService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private CountryRepository countryRepository;
    private CountriesNowConfig countriesNowConfig;
    private RestClient restClient;

    public CountryServiceImpl(CountryRepository countryRepository, CountriesNowConfig countriesNowConfig, RestClient restClient) {
        this.countryRepository = countryRepository;
        this.countriesNowConfig = countriesNowConfig;
        this.restClient = restClient;
    }

    @Override
    public void seedCountries() {
        if(!hasInitializedExRates()){
            updateCountries(fetchCountries());
        }

    }

    @Override
    @Cacheable("countriesCodes")
    public List<String> allCountryCodes() {
        return this.countryRepository.findAll()
                .stream()
                .map(Country::getCode)
                .toList();
    }

    public boolean hasInitializedExRates() {
        return this.countryRepository.count() > 0;
    }


    @Override
    public CountryDto[] fetchCountries() {
        return restClient
                .get()
                .uri(countriesNowConfig.getUrl())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(CountryDto[].class);

    }


    @Transactional
    @Override
    public void updateCountries(CountryDto[] countryDtos) {
        List<Country> countriesToUpdate = new ArrayList<>();

        for (CountryDto countryDto : countryDtos) {
            Optional<Country> existingCountry = countryRepository.findByName(countryDto.getName().getOfficial());

            if (existingCountry.isPresent()) {
                // Update existing country
                existingCountry.get().setCode(countryDto.getCca2());
                // Add more fields to update if necessary

                countriesToUpdate.add(existingCountry.get());
            } else {
                // Create new country
                Country newCountry = new Country();
                newCountry.setCode(countryDto.getCca2());
                newCountry.setName(countryDto.getName().getOfficial());
                // Add more fields to set if necessary
                countriesToUpdate.add(newCountry);
            }
        }
        countryRepository.saveAll(countriesToUpdate);
    }
}
