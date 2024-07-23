package com.softuni.crossfitapp.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.softuni.crossfitapp.domain.dto.events.EventDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipProfilePageDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.repository.*;
import com.softuni.crossfitapp.service.CloudinaryService;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.service.impl.CommentServiceImpl;
import com.softuni.crossfitapp.service.impl.UserServiceImpl;
import com.softuni.crossfitapp.testUtils.TestData;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Value("${mail.port}")
    private int port;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    private GreenMail greenMail;


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private TestData data;

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

    @MockBean
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TrainingRepository trainingRepository;
    private UserService userService;
    private CommentService commentService;


    @BeforeEach
    public void setUp(){
        commentService = new CommentServiceImpl(commentRepository,trainingRepository,userRepository,mapper);
        userService = new UserServiceImpl( applicationEventPublisher,  userRepository,  roleRepository,  membershipRepository,  countryRepository,
                 activationCodeRepository,  passwordEncoder,  cloudinaryService,  mapper);
        greenMail = new GreenMail(new ServerSetup(port, host,"smtp"));
        greenMail.start();
        greenMail.setUser(username, password);
        Mockito.when(cloudinaryService.uploadPhoto(any(org.springframework.web.multipart.MultipartFile.class), anyString())).thenReturn("http://mockurl.com/mockimage.jpg");
        data.createUser("testuser", "Ivo", "Ivov", "email@gmail.com", "08991612383", "DE", "Deutschland");
    }
    @AfterEach
    public void tearDown(){
        this.data.deleteAllTrainings();
        this.data.deleteUsers();
        this.data.deleteRoles();
        this.data.deleteCountries();
        greenMail.stop();

    }
    @Test
    public void testGetEditProfilePage() throws Exception {
        UserDetails userDetails = new CrossfitUserDetails("testuser", "user", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),"Ivo","Kamenov", UUID.randomUUID(),1L);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/profile/{username}/edit","testuser").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("updateProfilePage"))
                .andExpect(model().attributeExists("userProfileUpdateDto"));
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetProfilePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/profile/{username}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile-page"))
                .andExpect(model().attributeExists("userProfileDto"));
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/profile/{username}/edit/delete-profile", "testuser")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));;
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

    @Test
    @WithMockUser(username = "testuser",roles = {"USER","ADMIN"})
    public void getProfileDashboard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profiles-dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    @WithMockUser(username = "secondUser",roles = {"USER","ADMIN"})
    public void disableAccount() throws Exception {
        // Look the setUp
        User user = this.userRepository.findByUsername("testuser").get();

        mockMvc.perform(MockMvcRequestBuilders.patch("/disableAcc/{accountUUID}",user.getUuid())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles-dashboard"));
        // Wait for the email to be received
        greenMail.waitForIncomingEmail(1); // Increased wait time
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        Assertions.assertEquals(1, receivedMessages.length);

        MimeMessage receivedMessage = receivedMessages[0];
        Assertions.assertEquals("Your account has been suspended !", receivedMessage.getSubject());

        Assertions.assertEquals("crossfit-stuttgart@gmail.com", receivedMessage.getFrom()[0].toString());
        String body = GreenMailUtil.getBody(receivedMessage);

        Assertions.assertTrue(body.contains(user.getUsername()));
        Assertions.assertTrue(body.contains(user.getFirstName()));
        Assertions.assertTrue(body.contains(user.getUuid().toString()));

    }

    @Test
    @WithMockUser(username = "secondUser",roles = {"USER","ADMIN"})
    public void enableAccount() throws Exception {
        // Look the setUp
        User user = this.userRepository.findByUsername("testuser").get();

        mockMvc.perform(MockMvcRequestBuilders.patch("/enableAcc/{accountUUID}",user.getUuid())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles-dashboard"));
        // Wait for the email to be received
        greenMail.waitForIncomingEmail(1); // Increased wait time
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        Assertions.assertEquals(1, receivedMessages.length);

        MimeMessage receivedMessage = receivedMessages[0];
        Assertions.assertEquals("You are free to post comments again !", receivedMessage.getSubject());

        Assertions.assertEquals("crossfit-stuttgart@gmail.com", receivedMessage.getFrom()[0].toString());
        String body = GreenMailUtil.getBody(receivedMessage);

        Assertions.assertTrue(body.contains(user.getUsername()));
        Assertions.assertTrue(body.contains(user.getFirstName()));
        Assertions.assertTrue(body.contains(user.getUuid().toString()));

    }
    @Test
    void testRegistration() throws Exception {
        data.createCountry("BG","Bulgaria");
        MockMultipartFile mockFile = new MockMultipartFile(
                "photo",             // Name of the parameter
                "test.jpg",          // Original filename
                "image/jpeg",        // Content type
                "mock file content".getBytes() // Content as byte array
        );
        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/register")
                        .file(mockFile)
                        .param("email", "petrov2147@gmail.com")
                        .param("firstName", "Pesho")
                        .param("lastName", "Petrov")
                        .param("username", "sometesting")
                        .param("password", "Topsecret@12")
                        .param("confirmPassword", "Topsecret@12")
                        .param("address", "Some address")
                        .param("nationality", "BG")
                        .param("bornOn", "2005-01-01")
                        .param("telephoneNumber", "0897653123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/last-register-step"));


        greenMail.waitForIncomingEmail(1);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        Assertions.assertEquals(1, receivedMessages.length);
        MimeMessage registrationMessage = receivedMessages[0];

        Assertions.assertEquals(1, registrationMessage.getAllRecipients().length);
        Assertions.assertEquals("petrov2147@gmail.com", registrationMessage.getAllRecipients()[0].toString());

    }

    @Test
    @WithMockUser(username = "secondUser",roles = {"USER","ADMIN"})
    public void deleteComment() throws Exception {
        UUID commentUUID = this.data.createComment();

        // See the createComment() method
        User user = this.userRepository.findByUsername("testSecondUser").get();


        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteComment/{commentUUID}/{authorUsername}",commentUUID,user.getUsername())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles-dashboard"));
    }
}