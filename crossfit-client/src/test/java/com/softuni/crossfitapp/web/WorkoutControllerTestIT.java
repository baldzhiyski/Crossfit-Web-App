package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.trainings.TrainingDetailsDto;
import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutControllerTestIT {

    @Autowired
    private WorkoutsService workoutsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData data;


    @Test
    public void testGetWorkouts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/workouts"))
                .andExpect(status().isOk())
                .andExpect(view().name("workouts"));
    }

}