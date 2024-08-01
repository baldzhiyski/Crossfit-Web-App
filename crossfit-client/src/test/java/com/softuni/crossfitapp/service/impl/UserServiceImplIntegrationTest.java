package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.users.UserProfileDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.entity.Membership;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.*;
import com.softuni.crossfitapp.service.CloudinaryService;
import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplIntegrationTest {


    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Autowired
    private UserRepository userRepository;

    @Autowired

    private RoleRepository roleRepository;

    @Autowired

    private MembershipRepository membershipRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserActivationCodeRepository activationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private TestData testData;

    @Autowired
    private UserDetailsService userDetailsService;



    @BeforeEach
    public void setUp(){
        this.userService = new UserServiceImpl( applicationEventPublisher,  userRepository, userDetailsService, roleRepository,  membershipRepository,  countryRepository,  activationCodeRepository,
                 passwordEncoder,  cloudinaryService,  mapper);

    }

    @AfterEach
    public void tearDown(){
        testData.deleteUsers();
        testData.deleteRoles();
        testData.deleteMemberships();
    }

    @Test
    public void getProfilPageDtoTest(){
        User user = testData.createUser("testuser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");

        UserProfileDto testuser = this.userService.getProfilePageDto("testuser");

        assertNotNull(testuser);
        assertEquals(user.getEmail(),testuser.email());
        assertEquals(user.getFullName(),testuser.fullName());
    }


    @Test
    public void deleteAcc(){
        User user = testData.createUser("testuser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");
        assertEquals(1,this.userRepository.count());
        this.userService.deleteAcc(user.getUsername());
        assertEquals(0,this.userRepository.count());
    }


    @Test
    public void buyMembership(){
        User user = testData.createUser("testuser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");
        Membership membership = testData.createMembership();
        testData.createSecondRole();
        assertNull(this.userRepository.findByUsername("testuser").get().getMembership());

        this.userService.buyMembership(user.getUsername(),membership.getMembershipType());
        assertNotNull(this.userRepository.findByUsername("testuser").get().getMembership());
    }

    @Test
    public void getUsersExpiredMemberships(){
        testData.createUsersWithExpiredMembership();

        Set<User> allUsersWithExpiredMembership = this.userService.findAllUsersWithExpiredMembership();
        assertEquals(3,allUsersWithExpiredMembership.size());
    }

    @Test
    public void removeExpiredMembership(){
        User userAcc = testData.createUser("testuser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");
        Role role = this.roleRepository.saveAndFlush(new Role(RoleType.COACH));
        Role byRoleType = this.roleRepository.findByRoleType(RoleType.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        roles.add(byRoleType);
        userAcc.setRoles(roles);
        this.userRepository.saveAndFlush(userAcc);
        assertEquals(2, userAcc.getRoles().size());

        this.userService.removeExpiredMembership(userAcc);
        assertNull(userAcc.getMembership());
        assertEquals(1, userAcc.getRoles().size());
    }


    @Test
    public void updateProfile() throws IOException {
        User userAcc = testData.createUser("testuser", "Ivo", "Ivov", "email123@gmail.com", "08991612383", "DE", "Deutschland");
        UserProfileUpdateDto userProfileUpdateDto = new UserProfileUpdateDto();
        assertTrue(this.userRepository.findByUsername("testuser").isPresent());

        userProfileUpdateDto.setUsername("changedUsername");
        userProfileUpdateDto.setFirstName("Ivan");
        userProfileUpdateDto.setEmail("changedEmail123@abv.bg");

        this.userService.updateProfile(userAcc.getUsername(),userProfileUpdateDto);
        assertFalse(this.userRepository.findByUsername("testuser").isPresent());
        assertTrue(this.userRepository.findByUsername("changedUsername").isPresent());
        assertNotEquals(userAcc.getFirstName(),this.userRepository.findByUsername("changedUsername").get().getFirstName());
        assertNotEquals(userAcc.getEmail(),this.userRepository.findByUsername("changedUsername").get().getEmail());
        assertTrue(this.userRepository.findByUsername("changedUsername").get().getEmail().equals("changedEmail123@abv.bg"));
    }


}

