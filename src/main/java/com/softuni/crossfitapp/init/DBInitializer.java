package com.softuni.crossfitapp.init;

import com.softuni.crossfitapp.service.ExchangeRateService;
import com.softuni.crossfitapp.service.SeedService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer  implements CommandLineRunner {
    private SeedService seedService;
    private ExchangeRateService exchangeRateService;

    public DBInitializer(SeedService seedService, ExchangeRateService exchangeRateService) {
        this.seedService = seedService;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedService.seedAll();
        exchangeRateService.seedRates();
    }
}
