package com.softuni.crossfitapp.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;
@Getter
public class EnabledAccountEvent extends ApplicationEvent {
    private UUID uuid;
    private String username;
    private String fullName;
    private String email;
    public EnabledAccountEvent(Object source, UUID uuid, String username, String fullName, String email) {
        super(source);
        this.uuid = uuid;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }
}
