package com.softuni.crossfitapp.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softuni.crossfitapp.config.rest.WorkoutsAPIConfig;
import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.repository.TrainingRepository;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.service.impl.WorkoutServiceImpl;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    @AfterEach
    public void tearDown(){
        this.trainingRepository.deleteAll();
    }


    @BeforeEach
    public void beforeEach(){
        this.workoutsService = new WorkoutServiceImpl(trainingRepository,restClient,workoutsAPIConfig,mapper);
    }

    @Test
    public void testGetWorkouts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(view().name("workouts"));
    }
    @Test
    public void testExploreCurrentWorkout() throws Exception {
        Training testingWorkout = data.createTraining("Testing Workout", "images/test.jpeg", Level.INTERMEDIATE, TrainingType.HYROX);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/workouts/explore-current/{trainingType}", testingWorkout.getTrainingType())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "testuser",roles = {"USER"})
    public void postCommentTest() throws Exception {
        data.createUser();
        data.createTraining("Some test","images/test.jpeg",Level.INTERMEDIATE,TrainingType.CARDIO);
        // Mocked DTO data
        AddCommentDto addCommentDto = new AddCommentDto();
        addCommentDto.setDescription("Test comment");
        // Perform POST request with simulated data
        mockMvc.perform(MockMvcRequestBuilders.post("/workouts/details/comment/{trainingType}", TrainingType.CARDIO)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("description", addCommentDto.getDescription())
                        .with(csrf())
                       )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/workouts/explore-current/" + TrainingType.CARDIO));
    }

}