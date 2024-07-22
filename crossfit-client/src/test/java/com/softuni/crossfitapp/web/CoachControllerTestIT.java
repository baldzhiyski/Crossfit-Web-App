package com.softuni.crossfitapp.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.softuni.crossfitapp.domain.entity.Coach;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import com.softuni.crossfitapp.repository.CoachRepository;
import com.softuni.crossfitapp.repository.RoleRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.repository.WeeklyTrainingRepository;
import com.softuni.crossfitapp.service.CoachService;
import com.softuni.crossfitapp.service.impl.CoachServiceImpl;
import com.softuni.crossfitapp.testUtils.TestData;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CoachControllerTestIT {


    private CoachService coachService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private WeeklyTrainingRepository weeklyTrainingRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private TestData data;


    @Value("${mail.port}")
    private int port;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    private GreenMail greenMail;

    @BeforeEach
    public void setUp(){
        coachService = new CoachServiceImpl( applicationEventPublisher,  userRepository,
                roleRepository,  coachRepository,  weeklyTrainingRepository,  mapper);

        greenMail = new GreenMail(new ServerSetup(port, host,"smtp"));
        greenMail.start();
        greenMail.setUser(username, password);
    }

    @AfterEach
    public void tearDown(){
        data.deleteUsers();
        data.deleteWeeklyTrainingAndCoach();
        greenMail.stop();
    }

    @Test
    public void testGetCoachesPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/coaches"))
                .andExpect(status().isOk())
                .andExpect(view().name("coaches"));
    }

    @Test
    @WithMockUser(username = "coach",roles = {"USER","MEMBER","COACH"})
    public void testGetCoachSchedule() throws Exception {
        data.createCoachWithUserAcc();
        mockMvc.perform(MockMvcRequestBuilders.get("/my-weekly-schedule/{username}","coach")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("upcoming-trainings"));

    }

//    @PostMapping("/my-weekly-schedule/{username}/cancel-training/{weeklyTrainingId}")
//    public String cancelTrainig(@PathVariable String username,@PathVariable UUID weeklyTrainingId){
//        this.coachService.closeTrainingSession(username,weeklyTrainingId);
//que constraint or index violation ; SYS_CT_10117 table: USERS] [insert into users (address,born_on,country_id,email,first_name,image_url,is_active,last_name,membership_id,membership_end_date,membership_start_date,password,telephone_number,username,uuid,id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,default)]; SQL [insert into users (address,born_on,country_id,email,first_name,image_url,is_active,last_name,membership_id,membership_end_date,membership_start_date,password,telephone_number,username,uuid,id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,default)]; constraint [SYS_CT_10117]
//        return "redirect:/my-weekly-schedule/" + username;
//    }

    @Test
    @WithMockUser(username = "coach",roles = {"USER","MEMBER","COACH"})
    public void testCancelParticipation() throws Exception {
        WeeklyTraining weeklyTraining = data.createWeeklyTraining();
        User user1 = data.createUser("secondUser", "Gosho", "Goshov", "georgi@abv.bg", "087612345", "DE", "Deutschland");
        User user2 = data.createUser("thirdUser", "Gosho", "Goshov", "georgi123@abv.bg", "0876121145", "DE", "Deutschland");
        User user3 = data.createUser("forthUser", "Gosho", "Goshov", "georgi345@abv.bg", "087610045", "DE", "Deutschland");

        List<WeeklyTraining> weeklyTrainings = new ArrayList<>();
        weeklyTrainings.add(weeklyTraining);
        user1.setTrainingsPerWeekList(weeklyTrainings);
        user2.setTrainingsPerWeekList(weeklyTrainings);
        user3.setTrainingsPerWeekList(weeklyTrainings);

        this.userRepository.saveAndFlush(user1);
        this.userRepository.saveAndFlush(user2);
        this.userRepository.saveAndFlush(user3);

        mockMvc.perform(MockMvcRequestBuilders.post("/my-weekly-schedule/{username}/cancel-training/{weeklyTrainingId}",weeklyTraining.getCoach().getUsername(),weeklyTraining.getUuid())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-weekly-schedule/" + weeklyTraining.getCoach().getUsername()));


        greenMail.waitForIncomingEmail(3);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        Assertions.assertEquals(3, receivedMessages.length);

        for (MimeMessage receivedMessage : receivedMessages) {
            Assertions.assertEquals("Cancelled Training from the coach !", receivedMessage.getSubject());

            Assertions.assertEquals("crossfit-stuttgart@gmail.com", receivedMessage.getFrom()[0].toString());
            String body = GreenMailUtil.getBody(receivedMessage);

            Assertions.assertTrue(body.contains(weeklyTraining.getDayOfWeek().toString()));
            Assertions.assertTrue(body.contains(weeklyTraining.getTime().toString()));
            Assertions.assertTrue(body.contains(weeklyTraining.getDate().toString()));
        }


    }

}

