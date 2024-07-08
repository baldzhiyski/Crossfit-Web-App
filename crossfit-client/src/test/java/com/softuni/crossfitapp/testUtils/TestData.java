package com.softuni.crossfitapp.testUtils;

import com.softuni.crossfitapp.domain.entity.Country;
import com.softuni.crossfitapp.domain.entity.ExRateEntity;
import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.repository.CountryRepository;
import com.softuni.crossfitapp.repository.ExRateRepository;
import com.softuni.crossfitapp.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestData {

    @Autowired
    private ExRateRepository exchangeRateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    public void createExchangeRate(String currency, BigDecimal rate) {
        exchangeRateRepository.save(
                 ExRateEntity.builder().currency(currency).rate(rate).build()
        );
    }

    public void createTraining(String description, String imageUrl, Level level, TrainingType trainingType){
        trainingRepository.save(Training.builder().trainingType(trainingType).level(level).description(description).imageUrl(imageUrl).build());
    }

    public void createCountry(String code,String fullName){
        countryRepository.save(Country.builder().name(fullName).code(code).build());
    }

    public void cleanAllTestData() {
        exchangeRateRepository.deleteAll();
    }
}