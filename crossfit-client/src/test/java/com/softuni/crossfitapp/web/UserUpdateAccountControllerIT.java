package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.service.WorkoutsService;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserUpdateAccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;



    @Test
    public void testGetEditProfilePage() throws Exception {
        UserDetails userDetails = new CrossfitUserDetails("username", "user", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),"Ivo","Kamenov", UUID.randomUUID());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/profile/{username}/edit","testuser").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("updateProfilePage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userProfileUpdateDto"));
    }

  // TODO : Test cases for updating the account
}