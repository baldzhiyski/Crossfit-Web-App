package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.trainings.WeeklyTrainingDto;
import com.softuni.crossfitapp.domain.entity.*;
import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.CoachRepository;
import com.softuni.crossfitapp.repository.TrainingRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.repository.WeeklyTrainingRepository;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.WireMockHelpers.date;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class WorkoutServiceImplIT {

    @Autowired
    private WorkoutServiceImpl workoutService;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private WeeklyTrainingRepository weeklyTrainingRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private TestData testData;

    @AfterEach
    public void tearDown(){
        this.testData.deleteWeeklyTrainingAndCoach();
        this.testData.deleteUsers();
    }

    @Test
    @Transactional
    public void testPopulateWeeklyTrainings() {
        testData.createCoachWithUserAcc();
        populateTrainingsForEachType();
        workoutService.populateWeeklyTrainings();
        assertTrue(weeklyTrainingRepository.count() > 0, "Weekly trainings should be populated");
    }


    @Test
    @Transactional
    public void testJoinCurrentTraining() {
        // Prepare test data
        User user = testData.createUser("testuser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");
        Membership membership = testData.createMembership();
        user.setMembership(membership);

        userRepository.save(user);

        WeeklyTraining weeklyTraining = testData.createWeeklyTraining();

        // Perform action
        workoutService.joinCurrentTraining(weeklyTraining.getUuid(), user.getUsername());

        // Verify
        User updatedUser = userRepository.findByUsername(user.getUsername()).orElseThrow();
        assertTrue(updatedUser.getTrainingsPerWeekList().contains(weeklyTraining), "User should be joined to the training");
    }

    @Test
    @Transactional
    public void testDeleteFromCurrentTrainings() {
        User user = testData.createUser("testuser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");
        Membership membership = testData.createMembership();
        user.setMembership(membership);

        userRepository.save(user);

        WeeklyTraining weeklyTraining = testData.createWeeklyTraining();

        workoutService.joinCurrentTraining(weeklyTraining.getUuid(), user.getUsername());
        // Perform action
        workoutService.deleteFromCurrentTrainings(weeklyTraining.getUuid(), user.getUsername());
        // Verify
        User updatedUser = userRepository.findByUsername(user.getUsername()).orElseThrow();
        assertFalse(updatedUser.getTrainingsPerWeekList().contains(weeklyTraining), "User should be removed from the training");
    }

    @Test
    @Transactional
    public void testGetTrainingsWithDateBefore() {
        Coach coachWithUserAcc = this.testData.createCoachWithUserAcc();
        // Prepare test data
        WeeklyTraining pastTraining = createTestWeeklyTraining(LocalDate.now().minusDays(1));
        WeeklyTraining futureTraining = createTestWeeklyTraining(LocalDate.now().plusDays(1));
        WeeklyTraining todayTraining = createTestWeeklyTraining(LocalDate.now());

        pastTraining.setCoach(coachWithUserAcc);
        futureTraining.setCoach(coachWithUserAcc);
        todayTraining.setCoach(coachWithUserAcc);

        // Perform action
        List<WeeklyTraining> result = workoutService.getTrainingsWithDateBefore(LocalDate.now());

        // Verify
        assertEquals(1, result.size(), "There should be only one training with date before today");
        assertEquals(pastTraining.getDate(), result.get(0).getDate(), "The training date should match the past training date");
    }


    @Test
    public void testGetCurrentWeeklyTrainings(){
        User user = testData.createUser("testuser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");
        Membership membership = testData.createMembership();
        user.setMembership(membership);

        userRepository.save(user);

        WeeklyTraining weeklyTraining = testData.createWeeklyTraining();

        workoutService.joinCurrentTraining(weeklyTraining.getUuid(), user.getUsername());

        List<WeeklyTrainingDto> currentWeeklyTrainings = workoutService.getCurrentWeeklyTrainings(user.getUsername());
        assertEquals(1,currentWeeklyTrainings.size());

    }
    @Test
    @Transactional
    public void testGetWeeklyTrainingsSpecificDay() {
        // Prepare test data
        WeeklyTraining training = createTestWeeklyTraining(LocalDate.now().plusDays(1));

        // Perform action
        List<WeeklyTrainingDto> result = workoutService.getWeeklyTrainingsSpecificDay(training.getDayOfWeek());

        // Verify
        assertEquals(1, result.size(), "There should be two trainings on Monday");
        assertTrue(result.stream().allMatch(dto -> dto.getDayOfWeek().equals(DayOfWeek.MONDAY)), "All trainings should be on Monday");
    }

    private void populateTrainingsForEachType() {
        List<Training> trainings = new ArrayList<>();
        for (TrainingType value : TrainingType.values()) {
            Training training = new Training();
            training.setTrainingType(value);
            training.setDescription("Some test");
            training.setLevel(Level.BEGINNER);
            training.setImageUrl("test.jpeg");
            trainings.add(training);
        }
        this.trainingRepository.saveAllAndFlush(trainings);
    }

    private WeeklyTraining createTestWeeklyTraining(LocalDate localDate) {
        WeeklyTraining training = new WeeklyTraining();
        training.setUuid(UUID.randomUUID());
        training.setLevel(Level.BEGINNER);
        training.setTrainingType(TrainingType.WEIGHTLIFTING);
        training.setImageUrl("/images/weightlifting.jpg");
        training.setDayOfWeek(DayOfWeek.MONDAY);
        training.setDate(localDate);
        training.setTime(LocalTime.of(10, 0));
        return   this.weeklyTrainingRepository.saveAndFlush(training);

    }
}