package com.softuni.crossfitapp.testUtils;

import com.softuni.crossfitapp.domain.entity.*;
import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private MembershipRepository membershipRepository;


    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private WeeklyTrainingRepository weeklyTrainingRepository;


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

    public Membership createMembership(){
         return this.membershipRepository.saveAndFlush(Membership.builder().membershipType(MembershipType.ELITE).duration(3L).price(200L).build());
    }

    public void cleanAllTestData() {
        exchangeRateRepository.deleteAll();
    }

    @Transactional
    public User createUser(String username, String firstName, String lastName, String email, String telephoneNumber, String countryCode, String countryName) {
        Country country = createCountry(countryCode, countryName);
        if(this.roleRepository.count()==0) {
            createRole();
        }
        Role userRole = this.roleRepository.findByRoleType(RoleType.USER);

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

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
        user.setRoles(roles);
        user.setTrainingsPerWeekList(new ArrayList<>());
        user.setMembershipStartDate(LocalDate.now());
        user.setMembershipEndDate(LocalDate.now());
        user.setActive(true);

        return userRepository.saveAndFlush(user);
    }

    public Role createRole() {
        return this.roleRepository.saveAndFlush(new Role(RoleType.USER));
    }

    public Role createSecondRole() {
        return this.roleRepository.saveAndFlush(new Role(RoleType.MEMBER));
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

    @Transactional
    public void deleteMemberships() {
        this.membershipRepository.deleteAll();
    }

    @Transactional
    public void deleteWeeklyTrainingAndCoach() {
        this.weeklyTrainingRepository.deleteAll();
    }
    @Transactional
    public WeeklyTraining createWeeklyTraining() {
        Training training = createTraining();

        Coach coach = createCoachWithUserAcc();

        WeeklyTraining weeklyTraining = new WeeklyTraining();

        weeklyTraining.setTime(LocalTime.MIDNIGHT);
        weeklyTraining.setDate(LocalDate.now());
        weeklyTraining.setCoach(coach);
        weeklyTraining.setImageUrl("imag.jpeg");
        weeklyTraining.setParticipants(new ArrayList<>());
        weeklyTraining.setTrainingType(training.getTrainingType());
        weeklyTraining.setLevel(training.getLevel());
        weeklyTraining.setDayOfWeek(DayOfWeek.SUNDAY);

        this.coachRepository.saveAndFlush(coach);
        this.weeklyTrainingRepository.saveAndFlush(weeklyTraining);

        return weeklyTraining;
    }

    public Coach createCoachWithUserAcc() {
        Coach coach = new Coach();
        coach.setUsername("coach");
        coach.setFirstName("Peter");
        coach.setLastName("Ivanov");
        coach.setTrainingsPerWeek(new ArrayList<>());

        User userAcc = createUser("coach", "Petar", "Ivanov", "petar@abv.bg", "0899163112", "DE", "Deutschland");
        Role role = this.roleRepository.saveAndFlush(new Role(RoleType.COACH));
        Role byRoleType = this.roleRepository.findByRoleType(RoleType.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        roles.add(byRoleType);
        userAcc.setRoles(roles);
        this.userRepository.saveAndFlush(userAcc);
        this.coachRepository.saveAndFlush(coach);
        return coach;
    }

    @Transactional
    public void deleteCoaches() {
        this.coachRepository.deleteAll();
    }

    public void createUsersWithExpiredMembership() {
        User userAcc1 = createUser("user1", "Petar", "Ivanov", "petar1@abv.bg", "0899163113", "DE", "Deutschland");
        User userAcc2 = createUser("user2", "Petar", "Ivanov", "petar2@abv.bg", "0899163114", "DE", "Deutschland");
        User userAcc3 = createUser("user3", "Petar", "Ivanov", "petar3@abv.bg", "0899163115", "DE", "Deutschland");



    }
}