package com.softuni.crossfitapp.testUtils;

import com.softuni.crossfitapp.domain.entity.ExRateEntity;
import com.softuni.crossfitapp.repository.ExRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestData {

    @Autowired
    private ExRateRepository exchangeRateRepository;

    public void createExchangeRate(String currency, BigDecimal rate) {
        exchangeRateRepository.save(
                 ExRateEntity.builder().currency(currency).rate(rate).build()
        );
    }

    public void cleanAllTestData() {
        exchangeRateRepository.deleteAll();
    }
}