package com.softuni.crossfitapp.web.rest;

import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerIT {

    @Autowired
    private TestData testData;

    @Autowired
    private MockMvc mockMvc;
    private UUID commentId;

    @BeforeEach
    public void setUp(){
        this.testData.createTraining();
        this.testData.createUser("testuser", "Ivo", "Ivov", "email@gmail.com", "08991612383", "DE", "Deutschland");
        this.commentId = testData.createComment();
    }


    @Test
    @WithMockUser(username = "testuser")
    public void testPostComment() throws Exception {
        String jsonBody = "{\"description\": \"Test posting comment !\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/workouts/details/comment/{trainingType}", TrainingType.WOD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testLikeComment() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/workouts/details/{trainingType}/comment/like/{commentId}", TrainingType.WOD, commentId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likes", is(1))); // Assuming likes count starts at 0
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testDislikeComment() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/workouts/details/{trainingType}/comment/dislike/{commentId}", TrainingType.WOD, commentId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dislikes", is(1))); // Assuming dislikes count starts at 0
    }

    @Test
    @WithMockUser(username = "testSecondUser")
    public void testDeleteComment() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/workouts/details/{trainingType}/comment/{commentId}", TrainingType.WOD, commentId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @AfterEach
    public void tearDown(){
        this.testData.deleteAllComments();
        this.testData.deleteUsers();
        this.testData.deleteAllTrainings();
        this.testData.deleteRoles();
    }
}
