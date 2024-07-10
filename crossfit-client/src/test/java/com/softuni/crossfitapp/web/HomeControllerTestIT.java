package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.events.EventDetailsDto;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

//
//    @Autowired
//    private EventService eventService;


    @Test
    public void testIndex() throws Exception {
        UserDetails userDetails = new CrossfitUserDetails("username", "user", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),"Ivo","Kamenov", UUID.randomUUID());
        mockMvc.perform(MockMvcRequestBuilders.get("/").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }
//
//    @Test
//    public void testAbout() throws Exception {
//        List<EventDetailsDto> mockEvents = Arrays.asList(
//                new EventDetailsDto("Description 1", new Date(), "Address 1", "Event 1", "Video URL 1"),
//                new EventDetailsDto("Description 2", new Date(), "Address 2", "Event 2", "Video URL 2"),
//                new EventDetailsDto("Description 3", new Date(), "Address 3", "Event 3", "Video URL 3")
//        );
//
//        // Add mock events as needed for testing
//        when(eventService.getTopThreeEvents()).thenReturn(mockEvents);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/about-us"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("about"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("events"));
//    }

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