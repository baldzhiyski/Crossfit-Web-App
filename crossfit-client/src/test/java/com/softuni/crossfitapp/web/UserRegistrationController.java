package com.softuni.crossfitapp.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.softuni.crossfitapp.testUtils.TestData;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationController {
    @Autowired
    private MockMvc mockMvc;

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
    private TestData data;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host,"smtp"));
        greenMail.start();
        greenMail.setUser(username, password);
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
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
}
