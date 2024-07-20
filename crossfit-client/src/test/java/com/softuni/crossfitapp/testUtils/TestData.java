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
import java.util.*;

@Component
public class TestData {

    @Autowired
    private ExRateRepository exchangeRateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TrainingRepository trainingRepository;


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;

    public void createExchangeRate(String currency, BigDecimal rate) {
        exchangeRateRepository.save(
                 ExRateEntity.builder().currency(currency).rate(rate).build()
        );
    }

    public Training createTraining(){
        return trainingRepository.saveAndFlush(Training.builder().trainingType(TrainingType.WOD).level(Level.INTERMEDIATE).description("Test").imageUrl("test.pic").build());
    }

    public Country createCountry(String code,String fullName){
        return countryRepository.saveAndFlush(Country.builder().name(fullName).code(code).build());
    }

    public void cleanAllTestData() {
        exchangeRateRepository.deleteAll();
    }

    @Transactional
    public User createUser(String username, String firstName, String lastName, String email, String telephoneNumber, String countryCode, String countryName) {
        Country country = createCountry(countryCode, countryName);
        if(this.roleRepository.count()==0) {
            creteRole();
        }
        Role userRole = this.roleRepository.findByRoleType(RoleType.USER);

        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress("Test Address");
        user.setCountry(country);
        user.setBornOn(new Date()); // Adjust as per your date requirements
        user.setPassword("newPass@");
        user.setImageUrl("images/test.jpeg");
        user.setTelephoneNumber(telephoneNumber);
        user.setEmail(email);
        user.setUuid(UUID.randomUUID());
        user.setRoles(Set.of(userRole));
        user.setTrainingsPerWeekList(new ArrayList<>());
        user.setMembershipStartDate(LocalDate.now());
        user.setMembershipEndDate(LocalDate.now());
        user.setEvents(new HashSet<>());
        user.setActive(true);

        return userRepository.saveAndFlush(user);
    }

    public Role creteRole() {
        return this.roleRepository.saveAndFlush(new Role(RoleType.USER));
    }

    @Transactional
    public void deleteAllTrainings() {
        this.trainingRepository.deleteAll();
    }

    @Transactional
    public void deleteAllComments() {
        this.commentRepository.deleteAll();
    }

    @Transactional
    public void deleteUsers() {
        this.userRepository.deleteAll();
    }

    @Transactional
    public void deleteRoles(){
        this.roleRepository.deleteAll();
    }

    public int getCountComments() {
        return (int) this.commentRepository.count();
    }

    @Transactional
    public UUID createComment() {
        User logged = createUser("testSecondUser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");
        Training training;
        if(this.trainingRepository.count()==0) {
             training = createTraining();
        }else{
            training = this.trainingRepository.findByTrainingType(TrainingType.WOD).orElseThrow();
        }
        Comment comment = new Comment();
        comment.setDislikes(0);
        comment.setLikes(0);
        comment.setText("Some text");
        comment.setDate(LocalDate.now());
        comment.setAuthor(logged);
        comment.setUuid(UUID.randomUUID());
        comment.setTraining(training);
        return this.commentRepository.saveAndFlush(comment).getUuid();
    }
}