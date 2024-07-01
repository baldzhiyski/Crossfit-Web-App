package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.config.ForexAPIConfig;
import com.softuni.crossfitapp.domain.dto.exrates.ExRatesDTO;
import com.softuni.crossfitapp.domain.entity.ExRateEntity;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.ExRateRepository;
import com.softuni.crossfitapp.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);
    private final ExRateRepository exRateRepository;
    private final RestClient restClient;
    private final ForexAPIConfig forexApiConfig;

    @Autowired
    public ExchangeRateServiceImpl(ExRateRepository exRateRepository, RestClient restClient, ForexAPIConfig forexApiConfig) {
        this.exRateRepository = exRateRepository;
        this.restClient = restClient;
        this.forexApiConfig = forexApiConfig;
    }

    @Override
    public List<String> allSupportedCurrencies() {
        return this.exRateRepository.findAll()
                .stream()
                .map(ExRateEntity::getCurrency)
                .toList();
    }


    public boolean hasInitializedExRates() {
        return this.exRateRepository.count() > 0;
    }


    public ExRatesDTO fetchExRates() {
        return restClient
                .get()
                .uri(forexApiConfig.getUrl(), forexApiConfig.getKey())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ExRatesDTO.class);
    }


    public void updateRates(ExRatesDTO exRatesDTO) {
        LOGGER.info("Updating {} rates.", exRatesDTO.rates().size());

        if (!forexApiConfig.getBase().equals(exRatesDTO.base())) {
            throw new IllegalArgumentException("The exchange rates that should be updated are not based on " +
                    forexApiConfig.getBase() + " but rather on " + exRatesDTO.base());
        }

        exRatesDTO.rates().forEach((currency, rate) -> {
            var exRateEntity = exRateRepository.findByCurrency(currency)
                    .orElseGet(() -> {
                        ExRateEntity exRateEntity1 = new ExRateEntity();
                        exRateEntity1.setCurrency(currency);
                        return exRateEntity1;
                    });

            exRateEntity.setRate(rate);

            exRateRepository.save(exRateEntity);
        });
    }

    @Override
    public Optional<BigDecimal> findExRate(String from, String to) {
        if (Objects.equals(from, to)) {
            return Optional.of(BigDecimal.ONE);
        }

        // USD/BGN=x
        // USD/EUR=y

        // USD = x * BGN
        // USD = y * EUR

        // EUR/BGN = x / y

        Optional<BigDecimal> fromOpt = forexApiConfig.getBase().equals(from) ?
                Optional.of(BigDecimal.ONE) :
                exRateRepository.findByCurrency(from).map(ExRateEntity::getRate);

        Optional<BigDecimal> toOpt = forexApiConfig.getBase().equals(to) ?
                Optional.of(BigDecimal.ONE) :
                exRateRepository.findByCurrency(to).map(ExRateEntity::getRate);

        if (fromOpt.isEmpty() || toOpt.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(toOpt.get().divide(fromOpt.get(), 2, RoundingMode.HALF_DOWN));
        }
    }

    @Override
    public BigDecimal convert(String from, String to, BigDecimal amount) {
        return findExRate(from, to)
                .orElseThrow(() -> new ObjectNotFoundException("Conversion from " + from + " to " + to + " not possible!"))
                .multiply(amount);
    }

    @Override
    public void seedRates() {
        if(!hasInitializedExRates()){
            updateRates(fetchExRates());
        }
    }
}
