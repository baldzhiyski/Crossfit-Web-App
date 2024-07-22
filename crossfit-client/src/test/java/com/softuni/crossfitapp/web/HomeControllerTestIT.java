package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.config.rest.EventsAPIConfig;
import com.softuni.crossfitapp.domain.dto.events.EventDetailsDto;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.service.EventService;
import com.softuni.crossfitapp.service.impl.EventServiceImpl;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestClient;

import java.util.*;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTestIT {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private EventService eventService;

    @Autowired
    private TestData data;

    @AfterEach
    public void tearDown(){
        this.data.deleteUsers();
        this.data.deleteRoles();
    }

    @Test
    public void testIndex() throws Exception {
        UserDetails userDetails = new CrossfitUserDetails("testuser", "user", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),"Ivo","Kamenov", UUID.randomUUID());
        mockMvc.perform(MockMvcRequestBuilders.get("/").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }



    @Test
    public void testSchedule() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/schedule-for-the-week"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/users/login"));
    }

    @Test
    public void testAccessDenied() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/access-denied"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("access-denied"));
    }

    @Test
    public void testGetTips() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/nutrition-blog"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("nutrition-tips"));
    }
}