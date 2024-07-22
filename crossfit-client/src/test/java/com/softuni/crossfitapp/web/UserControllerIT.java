package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.events.EventDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipProfilePageDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.service.impl.UserServiceImpl;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private TestData data;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp(){
        data.createUser("testuser", "Ivo", "Ivov", "email@gmail.com", "08991612383", "DE", "Deutschland");

    }
    @AfterEach
    public void tearDown(){
        this.data.deleteUsers();
        this.data.deleteAllTrainings();
        this.data.deleteRoles();

    }
    @Test
    public void testGetEditProfilePage() throws Exception {
        UserDetails userDetails = new CrossfitUserDetails("testuser", "user", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),"Ivo","Kamenov", UUID.randomUUID());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/profile/{username}/edit","testuser").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("updateProfilePage"))
                .andExpect(model().attributeExists("userProfileUpdateDto"));
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetProfilePage() throws Exception {
        // Create a mock UserProfileDto
        MembershipProfilePageDto membershipDto = new MembershipProfilePageDto();
        UserProfileDto mockUserProfileDto = new UserProfileDto(
                "Ivo Ivov",
                "testuser",
                "images/test.jpeg",
                UUID.randomUUID(),
                "email@gmail.com",
                "Test Address",
                Set.of(RoleType.USER),
                membershipDto, // membership details if needed
                null  // enrolled trainings for the week if needed
        );

        // Mock the service method to return the mock UserProfileDto
        Mockito.when(userService.getProfilePageDto("testuser")).thenReturn(mockUserProfileDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/profile/{username}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile-page"))
                .andExpect(model().attributeExists("userProfileDto"))
                .andExpect(model().attribute("userProfileDto", mockUserProfileDto));
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/profile/{username}/edit/delete-profile", "testuser")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));;
        Mockito.verify(userService, Mockito.times(1)).deleteAcc(Mockito.eq("testuser"));
    }
    @Test
    public void confirmTabTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/last-register-step"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void activateUserTest() throws Exception {
        String activationCode = "test-activation-code";

        mockMvc.perform(MockMvcRequestBuilders.get("/activate/{activation_code}", activationCode))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/users/login"));
    }

    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username", ""))
                .andExpect(view().name("auth-login"));
    }

    @Test
    public void registerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerDto"))
                .andExpect(model().attributeExists("countryCodes"))
                .andExpect(view().name("auth-register"));
    }
}