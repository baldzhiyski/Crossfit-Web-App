package com.softuni.crossfitapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import com.softuni.crossfitapp.config.rest.CountriesNowConfig;
import com.softuni.crossfitapp.domain.dto.countries.CountryDto;
import com.softuni.crossfitapp.domain.dto.countries.Name;
import com.softuni.crossfitapp.domain.entity.Country;
import com.softuni.crossfitapp.repository.CountryRepository;
import com.softuni.crossfitapp.service.CountryService;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock(
        @ConfigureWireMock(name = "countries")
)
public class CountryServiceImplIT {

    @InjectWireMock("countries")
    private WireMockServer wireMockServer;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountriesNowConfig countriesNowConfig;

    @Autowired
    private TestData data;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        countriesNowConfig.setUrl(wireMockServer.baseUrl() + "/test-countries");
    }


    @Test
    void testFetchCountries_Success() throws JsonProcessingException {
        // Mock data from the external API
        CountryDto[] mockCountryDtos = {
                new CountryDto(new Name("United States"), "US"),
                new CountryDto(new Name("Canada"), "CA")
                // Add more mock data as needed
        };


        // Convert the mockCountryDtos array to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String mockResponse = objectMapper.writeValueAsString(mockCountryDtos);


        // Stub WireMock server to return mock response
        wireMockServer.stubFor(get("/test-countries")
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(mockResponse)));

        // Execute the service method
        CountryDto[] countries =this.countryService.fetchCountries() ;

        // Assertions
        assertEquals(2, countries.length);
        assertEquals("United States", countries[0].getName().getOfficial());
        assertEquals("US", countries[0].getCca2());
        assertEquals("Canada", countries[1].getName().getOfficial());
        assertEquals("CA", countries[1].getCca2());
    }
    @Test
    @Transactional
    void testUpdateCountries() {
        // Mock data to update
        CountryDto[] mockCountryDtos = {
                new CountryDto(new Name("United States"), "USA"),
                new CountryDto(new Name("Canada"), "CA"),
                new CountryDto(new Name("Mexico"), "MX")
        };
        Country newCountry2 = data.createCountry("US", "United States");

        assertEquals("US",this.countryRepository.findByName("United States").get().getCode());

        this.countryService.updateCountries(mockCountryDtos);

        assertEquals("USA",this.countryRepository.findByName("United States").get().getCode());
        assertEquals(3,this.countryRepository.count());
    }

}
