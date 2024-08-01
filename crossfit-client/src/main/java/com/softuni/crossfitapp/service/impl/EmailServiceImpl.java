package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.WeeklyTraining;
import com.softuni.crossfitapp.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String crossfitEmail;

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender javaMailSender, @Value("${mail.crossfit}") String crossfitEmail) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.crossfitEmail = crossfitEmail;
    }

    @Override
    public void sendRegistrationEmail(String userEmail, String userName, String activationCode, User savedUser) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {

            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(crossfitEmail);
            mimeMessageHelper.setReplyTo(crossfitEmail);
            mimeMessageHelper.setSubject("Welcome to Crossfit Stuttgart!");
            mimeMessageHelper.setText(generateRegistrationEmailBody(userName, activationCode,savedUser), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendCancelledTrainingEmail(String coachFullName, WeeklyTraining weeklyTraining, String userEmail, String userFullName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {

            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(crossfitEmail);
            mimeMessageHelper.setReplyTo(crossfitEmail);
            mimeMessageHelper.setSubject("Cancelled Training from the coach !");
            mimeMessageHelper.setText(generateCancelledTrainingBody(coachFullName,weeklyTraining,userFullName ), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendDisabledAccountEmail(UUID authorUUID, String username, String fullName, String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(crossfitEmail);
            mimeMessageHelper.setReplyTo(crossfitEmail);
            mimeMessageHelper.setSubject("Your account has been suspended !");
            mimeMessageHelper.setText(generateDisabledAccountBody(authorUUID,username,fullName ), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEnabledAccount(UUID authorUUID, String username, String fullName, String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(crossfitEmail);
            mimeMessageHelper.setReplyTo(crossfitEmail);
            mimeMessageHelper.setSubject("You are free to post comments again !");
            mimeMessageHelper.setText(generateEnableAccount(authorUUID,username,fullName ), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendUserLoggedViaGitHubEmailWithRawRandomPass(UUID uuid, String username, String fullName, String email, String currentPassword, String address, Date dateOfBirth) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(crossfitEmail);
            mimeMessageHelper.setReplyTo(crossfitEmail);
            mimeMessageHelper.setSubject("Warning for log in without account via GitHub !");
            mimeMessageHelper.setText(generateLoggedViaGitHub(uuid,username,fullName,email,currentPassword,address,dateOfBirth ), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateLoggedViaGitHub(UUID uuid, String username, String fullName, String email, String currentPassword, String address, Date dateOfBirth) {
        Context context = new Context();
        context.setVariable("uuid",uuid);
        context.setVariable("username",username);
        context.setVariable("fullName",fullName);
        context.setVariable("email",email);
        context.setVariable("currentPassword",currentPassword);
        context.setVariable("address",address);
        context.setVariable("dateOfBirth",dateOfBirth);
        return templateEngine.process("email/github-account-registration", context);
    }

    private String generateEnableAccount(UUID authorUUID, String username, String fullName) {
        Context context = new Context();
        context.setVariable("userFullName", fullName);
        context.setVariable("username", username);
        context.setVariable("authorUUID",authorUUID);

        return templateEngine.process("email/enable-account", context);
    }


    private String generateDisabledAccountBody(UUID authorUUID, String username, String fullName) {
        Context context = new Context();
        context.setVariable("userFullName", fullName);
        context.setVariable("username", username);
        context.setVariable("authorUUID",authorUUID);

        return templateEngine.process("email/disabled-account", context);
    }

    private String generateCancelledTrainingBody(String coachFullName, WeeklyTraining weeklyTraining, String userFullName) {
        Context context = new Context();
        context.setVariable("userFullName", userFullName);
        context.setVariable("coachFullName", coachFullName);
        context.setVariable("trainingDayOfWeek", weeklyTraining.getDayOfWeek());
        context.setVariable("trainingTime", weeklyTraining.getTime());
        context.setVariable("trainingDate", weeklyTraining.getDate());

        return templateEngine.process("email/cancelled-training", context);
    }

    private String generateRegistrationEmailBody(String userName, String activationCode, User savedUser) {

        Context context = new Context();
        context.setVariable("username", userName);
        context.setVariable("activation_code", activationCode);
        context.setVariable("username", savedUser.getUsername());

        return templateEngine.process("email/registration-email", context);

    }
}
