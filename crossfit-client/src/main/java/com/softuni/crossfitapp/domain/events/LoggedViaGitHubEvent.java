package com.softuni.crossfitapp.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;
import java.util.UUID;

@Getter
public class LoggedViaGitHubEvent extends ApplicationEvent {
    private UUID uuid;
    private String username;
    private String fullName;
    private String email;
    private String currentPassword;
    private String address;
    private Date dateOfBirth;
    public LoggedViaGitHubEvent(Object source, UUID uuid, String username, String fullName, String email, String currentPassword, String address, Date dateOfBirth) {
        super(source);
        this.uuid = uuid;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.currentPassword = currentPassword;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
