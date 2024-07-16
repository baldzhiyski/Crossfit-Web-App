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
