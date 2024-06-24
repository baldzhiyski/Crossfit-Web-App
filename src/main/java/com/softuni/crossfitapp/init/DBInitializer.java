package com.softuni.crossfitapp.init;

import com.softuni.crossfitapp.service.SeedService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer  implements CommandLineRunner {
    private SeedService seedService;

    public DBInitializer(SeedService seedService) {
        this.seedService = seedService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedService.seedAll();
    }
}
