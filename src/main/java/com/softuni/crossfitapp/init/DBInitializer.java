package com.softuni.crossfitapp.init;

import com.softuni.crossfitapp.service.CountryService;
import com.softuni.crossfitapp.service.ExchangeRateService;
import com.softuni.crossfitapp.service.SeedService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer  implements CommandLineRunner {
    private SeedService seedService;
    private ExchangeRateService exchangeRateService;

    private CountryService countryService;

    public DBInitializer(SeedService seedService, ExchangeRateService exchangeRateService, CountryService countryService) {
        this.seedService = seedService;
        this.exchangeRateService = exchangeRateService;
        this.countryService = countryService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedService.seedAll();
        exchangeRateService.seedRates();
        countryService.seedCountries();
    }
}
