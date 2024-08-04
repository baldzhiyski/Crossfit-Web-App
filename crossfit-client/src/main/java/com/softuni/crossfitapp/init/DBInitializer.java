package com.softuni.crossfitapp.init;

import com.softuni.crossfitapp.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "forex.api.init-exchange-rates", havingValue = "true")

public class DBInitializer  implements CommandLineRunner {
    private SeedService seedService;
    private ExchangeRateService exchangeRateService;

    private CountryService countryService;

    private WorkoutsService workoutsService;
    public DBInitializer(SeedService seedService, ExchangeRateService exchangeRateService, CountryService countryService, WorkoutsService workoutsService) {
        this.seedService = seedService;
        this.exchangeRateService = exchangeRateService;
        this.countryService = countryService;
        this.workoutsService = workoutsService;
    }

    @Override
    public void run(String... args) throws Exception {
        countryService.seedCountries();
        workoutsService.seedTrainings();
        seedService.seedAll();

//        this.workoutsService.populateWeeklyTrainings();;
    }
}
