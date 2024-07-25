package com.softuni.crossfitcommunityevents.config;

import com.softuni.crossfitcommunityevents.repository.EventRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class AppConfig {



    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource,
                                                       EventRepository eventRepository,
                                                       ResourceLoader resourceLoader) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        if (eventRepository.count() == 0) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(resourceLoader.getResource("classpath:data.sql"));
            initializer.setDatabasePopulator(populator);
        }

        return initializer;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();

        openAPI.setInfo(
                new Info()
                        .description("This is a crossfit events  micro service for helping the crossfit community.")
                        .title("Open Crossfit Events API")
                        .version("0.0.1")
                        .contact(
                                new Contact()
                                        .name("Baldzhiyski")
                                        .email("crossfit-stuttgart@gamil.com")
                        )
        );

        return openAPI;
    }

}
