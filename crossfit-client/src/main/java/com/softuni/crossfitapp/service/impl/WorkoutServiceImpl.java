package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.config.rest.WorkoutsAPIConfig;
import com.softuni.crossfitapp.domain.dto.trainings.SeedTrainingFromApiDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.dto.trainings.WeeklyTrainingDto;
import com.softuni.crossfitapp.domain.entity.*;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.exceptions.*;
import com.softuni.crossfitapp.repository.*;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.util.Constants;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.softuni.crossfitapp.util.Constants.*;

@Service
public class WorkoutServiceImpl implements WorkoutsService {
    private final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);
    private TrainingRepository trainingRepository;

    private WeeklyTrainingRepository weeklyTrainingRepository;

    private CoachRepository coachRepository;

    private UserRepository userRepository;
    private RestClient trainingsRestClient;

    private WorkoutsAPIConfig workoutsAPIConfig;

    private ModelMapper mapper;

    public WorkoutServiceImpl(TrainingRepository trainingRepository, WeeklyTrainingRepository weeklyTrainingRepository, CoachRepository coachRepository, UserRepository userRepository, @Qualifier("trainingsRestClient") RestClient trainingsRestClient, WorkoutsAPIConfig workoutsAPIConfig, ModelMapper mapper) {
        this.trainingRepository = trainingRepository;
        this.weeklyTrainingRepository = weeklyTrainingRepository;
        this.coachRepository = coachRepository;
        this.userRepository = userRepository;
        this.trainingsRestClient = trainingsRestClient;
        this.workoutsAPIConfig = workoutsAPIConfig;
        this.mapper = mapper;
    }

    public boolean hasInitializedWorkouts() {
        return this.trainingRepository.count() > 0;
    }


    @Override
    public Set<SeedTrainingFromApiDto> getAllTrainings() {
        LOGGER.info("Getting the trainings from the db ....");
        Set<SeedTrainingFromApiDto> body = trainingsRestClient
                .get()
                .uri(workoutsAPIConfig.getAllTrainingsUrl())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return body;
    }

    @Override
    @Transactional
    public void seedTrainings() {
        LOGGER.info("Seeding is beginning....");

        if(!hasInitializedWorkouts()){
            getAllTrainings().stream().map(seedTrainingFromApiDto -> {
                Training mapped = this.mapper.map(seedTrainingFromApiDto, Training.class);
                setSpecificPictureUrl(mapped);
                return mapped;
            }).forEach(this.trainingRepository::saveAndFlush);
        }

    }

    @Override
    public TrainingDetailsDto getTrainingDto(TrainingType trainingType) {
        Training training = this.trainingRepository
                .findByTrainingType(trainingType)
                .orElseThrow(() -> new ObjectNotFoundException("No such training specified in the crossfit community !"));

        return mapper.map(training, TrainingDetailsDto.class);
    }
    @Override
    public void populateWeeklyTrainings() {
        if(this.weeklyTrainingRepository.count()>0) {
         this.weeklyTrainingRepository.deleteAllWeeklyTrainings();

        }
        LocalDate nextMonday = LocalDate.now();

        // Check if today is not Monday
        if (nextMonday.getDayOfWeek() != DayOfWeek.MONDAY) {
            // Calculate days until next Monday
            int daysUntilNextMonday = DayOfWeek.MONDAY.getValue() - nextMonday.getDayOfWeek().getValue();
            if (daysUntilNextMonday < 0) {
                daysUntilNextMonday += 7;
            }
            nextMonday = nextMonday.plusDays(daysUntilNextMonday);
        }

        // Iterate over each day of the week starting from monday ( every week )
        for (int i = 0; i < 6; i++) {
            LocalDate currentTrainingDate = nextMonday.plusDays(i);
            DayOfWeek dayOfWeek = currentTrainingDate.getDayOfWeek();

            // Create four trainings for each day
            for (int j = 0; j < 4; j++) {
                // Get a random training from the repository
                Training randomTraining = getRandomTraining();

                // Create a new WeeklyTraining instance
                WeeklyTraining weeklyTraining = new WeeklyTraining();
                weeklyTraining.setLevel(randomTraining.getLevel());
                weeklyTraining.setTrainingType(randomTraining.getTrainingType());
                weeklyTraining.setImageUrl(randomTraining.getImageUrl());
                weeklyTraining.setDayOfWeek(dayOfWeek);
                weeklyTraining.setDate(currentTrainingDate);

                weeklyTraining.setTime(getRandomTime()); // Implement this method

                Coach randomCoach = getRandomCoach();
                randomCoach.getTrainingsPerWeek().add(weeklyTraining);

                weeklyTraining.setCoach(randomCoach); // Implement this method

                // Save the WeeklyTraining instance
                weeklyTrainingRepository.save(weeklyTraining);
                coachRepository.save(randomCoach);
            }
        }
    }

    @Override
    public List<WeeklyTrainingDto> getWeeklyTrainingsSpecificDay(DayOfWeek dayOfWeek) {

        return this.weeklyTrainingRepository.findAllByDayOfWeek(dayOfWeek)
                .stream()
                .map(weeklyTraining -> this.mapper.map(weeklyTraining,WeeklyTrainingDto.class))
                .sorted(Comparator.comparing(WeeklyTrainingDto::getTime))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void joinCurrentTraining(UUID trainingId, String loggedUserUsername) {
        WeeklyTraining weeklyTraining = this.weeklyTrainingRepository.findByUuid(trainingId).orElseThrow(() -> new ObjectNotFoundException("Ïnvalid weekly training id!"));
        User currentUser = this.userRepository.findByUsername(loggedUserUsername).orElseThrow(() -> new ObjectNotFoundException("Invalid logged user credentials !"));


        validate(currentUser, weeklyTraining);
        currentUser.getTrainingsPerWeekList().add(weeklyTraining);
        currentUser.setWeeklyTrainingsCount(currentUser.getWeeklyTrainingsCount()-1);

        this.userRepository.saveAndFlush(currentUser);
    }

    @Override
    public List<WeeklyTrainingDto> getCurrentWeeklyTrainings(String loggedUserUsername) {
        User currentUser = this.userRepository.findByUsername(loggedUserUsername).orElseThrow(() -> new ObjectNotFoundException("Invalid logged user credentials !"));

        return currentUser.getTrainingsPerWeekList()
                .stream()
                .map(weeklyTraining -> this.mapper.map(weeklyTraining, WeeklyTrainingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFromCurrentTrainings(UUID trainingId, String loggedUserUsername) {
        User currentUser = this.userRepository.findByUsername(loggedUserUsername).orElseThrow(() -> new ObjectNotFoundException("Invalid logged user credentials !"));
        WeeklyTraining weeklyTraining = this.weeklyTrainingRepository.findByUuid(trainingId).orElseThrow(() -> new ObjectNotFoundException("Ïnvalid weekly training id!"));

        // If we want do delete 3 hours before the training , we cannot !!!
        if (isWithinThreeHours(weeklyTraining.getDate(),weeklyTraining.getTime())) {
            throw new CannotDeleteTrainingException("You cannot delete a training session within 3 hours of its start time!");
        }

        currentUser.setWeeklyTrainingsCount(currentUser.getWeeklyTrainingsCount() + 1);
        currentUser.getTrainingsPerWeekList().remove(weeklyTraining);
        this.userRepository.saveAndFlush(currentUser);

    }


    // If a user tries to delete training that will start in 3 or less hours , an exception will be thrown
    private boolean isWithinThreeHours(LocalDate date, LocalTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime trainingStartDateTime = LocalDateTime.of(date, startTime);

        // Check if the current time is within 3 hours of the training start time
        return now.isAfter(trainingStartDateTime.minusHours(3));
    }

    @Override
    public List<WeeklyTraining> getTrainingsWithDateBefore(LocalDate currentDate, LocalTime localTime) {
        return weeklyTrainingRepository.findAll().stream()
                .filter(training -> training.getDate().isBefore(currentDate) || (training.getDate().equals(currentDate) && training.getTime().isBefore(localTime)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void doDeletion(List<WeeklyTraining> pastTrainings) {

        pastTrainings.forEach(training -> {
            // Remove from coach's list
            Coach coach = training.getCoach();
            coach.getTrainingsPerWeek().remove(training);
            // Remove from users' lists
            List<User> participants = training.getParticipants();


            participants.forEach(participant -> participant.getTrainingsPerWeekList().remove(training));
            userRepository.saveAllAndFlush(participants);
            coachRepository.saveAndFlush(coach);


            // We do not delete the training itself , it will be checked as already in the past through the js
        });

    }

    @Override
    public TrainingType getTrainingTypeById(UUID trainingId) {
        return this.weeklyTrainingRepository.findByUuid(trainingId).orElseThrow(()->new ObjectNotFoundException("No such training!")).getTrainingType();
    }

    private LocalTime getRandomTime() {
        int randomHour = ThreadLocalRandom.current().nextInt(8, 21);

        return LocalTime.of(randomHour,0);
    }

    private Training getRandomTraining() {
        TrainingType[] types = TrainingType.values();
        Random random = new Random();
        int index = random.nextInt(types.length-1);
       return this.trainingRepository.findByTrainingType(types[index]).orElseThrow(()->new ObjectNotFoundException("No such training type !"));
    }

    private Coach getRandomCoach() {
        return coachRepository.getRandomCoach();
    }

    private void setSpecificPictureUrl(Training mapped) {
        switch (mapped.getTrainingType()){
            case WEIGHTLIFTING -> mapped.setImageUrl(WEIGHTLIFTING_IMAGE_URL);
            case WOD ->  mapped.setImageUrl(WOD_IMAGE_URL);
            case HYROX ->  mapped.setImageUrl(HYROX_IMAGE_URL);
            case CARDIO -> mapped.setImageUrl(CARDIO_IMAGE_URL);
            case MUMFIT -> mapped.setImageUrl(MUMFIT_IMAGE_URL);
            case OPENGYM -> mapped.setImageUrl(OPENGYM_IMAGE_URL);
            case GYMNASTICS -> mapped.setImageUrl(GYMNASTICS_IMAGE_URL);
            default -> mapped.setImageUrl(DEFAULT_IMAGE_URL);
        }
    }

    private static void validate(User currentUser,WeeklyTraining weeklyTraining) {
        // If the plan is not supporting more trainings than the user wants , go to the access denied page
        if(currentUser.getWeeklyTrainingsCount() == 0){
            throw new WeeklyTrainingsExhaustedException("You have used all your trainings for this week!");
        }

        if(weeklyTraining.getParticipants().size() ==5){
            throw new FullTrainingCapacityException("There are no more spots available in the training !");
        }
    }
}
