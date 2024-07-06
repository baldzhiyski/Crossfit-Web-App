package com.softuni.crossfitapp.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {

    private final String userEmail;
    private final String userNames;

    public UserRegisteredEvent(Object source,
                               String userEmail,
                               String userNames) {
        super(source);
        this.userEmail = userEmail;
        this.userNames = userNames;
    }
}