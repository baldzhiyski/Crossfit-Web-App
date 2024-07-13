package com.softuni.crossfitapp.testUtils;

import com.softuni.crossfitapp.domain.entity.*;
import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class TestData {

    @Autowired
    private ExRateRepository exchangeRateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;

    public void createExchangeRate(String currency, BigDecimal rate) {
        exchangeRateRepository.save(
                 ExRateEntity.builder().currency(currency).rate(rate).build()
        );
    }

    public Training createTraining(String description, String imageUrl, Level level, TrainingType trainingType){
        return trainingRepository.save(Training.builder().trainingType(trainingType).level(level).description(description).imageUrl(imageUrl).build());
    }

    public Country createCountry(String code,String fullName){
        return countryRepository.saveAndFlush(Country.builder().name(fullName).code(code).build());
    }

    public void cleanAllTestData() {
        exchangeRateRepository.deleteAll();
    }

    @Transactional
    public User createUser() {
        Country country = createCountry("BG", "Bulgaria"); // Assuming createCountry method exists
        Role userRole = creteRole(); // Assuming createRole method exists

        User user = new User();
        user.setUsername("testUser");
        user.setFirstName("Ivo");
        user.setLastName("Ivov");
        user.setAddress("Test Address");
        user.setCountry(country);
        user.setBornOn(new Date()); // Adjust as per your date requirements
        user.setPassword("newPass@");
        user.setImageUrl("images/test.jpeg");
        user.setTelephoneNumber("0899162883");
        user.setEmail("email@gmail.com");
        user.setRoles(Set.of(userRole));// Initialize membership appropriately
        user.setTrainingsPerWeekList(new ArrayList<>());
        user.setMembershipStartDate(LocalDate.now());
        user.setMembershipEndDate(LocalDate.now());
        user.setEvents(new HashSet<>());
        user.setActive(true);

        return userRepository.saveAndFlush(user);
    }

    private Role creteRole() {
        return this.roleRepository.saveAndFlush(new Role(RoleType.USER));
    }
}