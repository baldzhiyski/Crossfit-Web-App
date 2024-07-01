package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.exrates.ExRatesDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateService {
    List<String> allSupportedCurrencies();

    Optional<BigDecimal> findExRate(String from, String to);

    BigDecimal convert(String from, String to, BigDecimal amount);

    void seedRates();
}
