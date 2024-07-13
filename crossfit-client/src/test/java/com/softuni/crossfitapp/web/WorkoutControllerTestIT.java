package com.softuni.crossfitapp.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.repository.TrainingRepository;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutControllerTestIT {

    @Autowired
    private WorkoutsService workoutsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TrainingRepository trainingRepository;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData data;

    @AfterEach
    public void tearDown(){
        this.trainingRepository.deleteAll();
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

//    @Test
//    public void testPostComment() throws Exception {
//
//        this.data.createUser();
//        String trainingType = "HYROX";
//        AddCommentDto addCommentDto = new AddCommentDto();
//        addCommentDto.setDescription("Great workout!");
//
//        // Mock the behavior of loadUserByUsername
//        CrossfitUserDetails userDetails = new CrossfitUserDetails("testUser", "password",
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
//                "John", "Doe", UUID.randomUUID()); // Replace with actual user details
//
//        Mockito.when(crossfitUserDetails.loadUserByUsername("testUser"))
//                .thenReturn(userDetails);
//
//        // Perform the POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/workouts/details/comment/{trainingType}", trainingType)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/workouts/explore-current/" + trainingType));
//    }

//    @Test
//    @WithMockUser(username = "testuser",roles = "{USER,COACH}")
//    public void postCommentTest() throws Exception {
//        // Mocked DTO data
//        AddCommentDto addCommentDto = new AddCommentDto();
//        addCommentDto.setDescription("Test comment");
//
//        UserDetails userDetails = new CrossfitUserDetails("username", "user", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),"Ivo","Kamenov", UUID.randomUUID());
//        // Perform POST request with simulated data
//        mockMvc.perform(MockMvcRequestBuilders.post("/workouts/details/comment/{trainingType}", TrainingType.CARDIO)
//                        .param("comment", addCommentDto.getDescription()))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/workouts/explore-current/" + TrainingType.CARDIO));
//
//        // Verify that addComment method was called with the correct parameters
//        verify(commentService).addComment(addCommentDto, TrainingType.CARDIO);
//    }

}