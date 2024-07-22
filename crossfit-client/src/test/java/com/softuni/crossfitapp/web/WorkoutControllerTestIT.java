package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.config.rest.WorkoutsAPIConfig;
import com.softuni.crossfitapp.domain.entity.*;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.repository.*;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.service.impl.WorkoutServiceImpl;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutControllerTestIT {

    private WorkoutsService workoutsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TrainingRepository trainingRepository;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData data;

    @Autowired
    private RestClient restClient;

    @Autowired
    private WorkoutsAPIConfig workoutsAPIConfig;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private WeeklyTrainingRepository weeklyTrainingRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RestClient trainingsRestClient;


    @BeforeEach
    public void setup(){
        this.workoutsService = new WorkoutServiceImpl( trainingRepository,  weeklyTrainingRepository,  coachRepository,  userRepository,
                trainingsRestClient,  workoutsAPIConfig,  mapper);
        data.createUser("testuser", "Ivo", "Ivov", "email@gmail.com", "08991612383", "DE", "Deutschland");
    }

    @AfterEach
    public void tearDown(){
        data.deleteAllTrainings();
        data.deleteUsers();
        data.deleteRoles();
        data.deleteMemberships();
        data.deleteWeeklyTrainingAndCoach();
    }

    @Test
    @WithMockUser(username = "testuser",roles = {"USER","MEMBER"})
    public void testSchedule() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/schedule-for-the-week"))
                .andExpect(status().isOk())
                .andExpect(view().name("schedule"));
    }
    @Test
    public void testScheduleWithoutAcc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/schedule-for-the-week"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testGetWorkouts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(view().name("workouts"));
    }
    @Test
    @WithMockUser(username = "testuser")
    public void testExploreCurrentWorkout() throws Exception {
        Training testingWorkout = data.createTraining();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/workouts/explore-current/{trainingType}", testingWorkout.getTrainingType())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }
    @Test
    @WithMockUser(username = "testuser",roles = {"USER","MEMBER"})
    public void testJoinInTraining() throws Exception {
        WeeklyTraining weeklyTraining = data.createWeeklyTraining();

        User user = this.userRepository.findByUsername("testuser").orElseThrow();
        user.setMembership(data.createMembership());
        this.userRepository.saveAndFlush(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/joinTraining/{trainingId}",weeklyTraining.getUuid())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/schedule-for-the-week"));

    }

//
//    @PostMapping("/my-workouts/delete-training/{trainingId}")
//    public String deleteFromMyTrainings(@PathVariable UUID trainingId,@AuthenticationPrincipal UserDetails userDetails){
//        this.workoutsService.deleteFromCurrentTrainings(trainingId,userDetails.getUsername());
//        return "redirect:/my-workouts";
//    }

    @Test
    @WithMockUser(username = "testuser",roles = {"USER","MEMBER"})
    public void testGetMyWorkouts() throws Exception {
        WeeklyTraining weeklyTraining = data.createWeeklyTraining();

        User user = this.userRepository.findByUsername("testuser").orElseThrow();
        user.setMembership(data.createMembership());


        weeklyTraining.setParticipants(List.of(user));
        user.getTrainingsPerWeekList().add(weeklyTraining);
        this.userRepository.saveAndFlush(user);
        this.weeklyTrainingRepository.saveAndFlush(weeklyTraining);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/my-workouts"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-trainings")).andReturn();

        List<WeeklyTraining> currentWeeklyTrainings = (List<WeeklyTraining>) result.getModelAndView().getModel().get("currentWeeklyTrainings");
        Assertions.assertEquals(1, currentWeeklyTrainings.size());

    }

    @Test
    @WithMockUser(username = "testuser",roles = {"USER","MEMBER"})
    public void testDeleteAddedTraining() throws Exception {
        WeeklyTraining weeklyTraining = data.createWeeklyTraining();

        User user = this.userRepository.findByUsername("testuser").orElseThrow();
        user.setMembership(data.createMembership());


        weeklyTraining.setParticipants(List.of(user));
        user.getTrainingsPerWeekList().add(weeklyTraining);
        this.userRepository.saveAndFlush(user);
        this.weeklyTrainingRepository.saveAndFlush(weeklyTraining);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/my-workouts/delete-training/{trainingId}", weeklyTraining.getUuid())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-workouts"))
                .andReturn();

        Assertions.assertEquals(null, result.getModelAndView().getModel().get("currentWeeklyTrainings"));


    }



}