package com.softuni.crossfitapp.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class CancelledTrainingEvent  extends ApplicationEvent {

    private String coachFullName;
    private UUID weeklyTrainingId;
    private String userEmail;
    private String userFullName;
    public CancelledTrainingEvent(Object source, String coachFullName, UUID weeklyTrainingId, String userEmail, String userFullName) {
        super(source);
        this.coachFullName = coachFullName;
        this.weeklyTrainingId = weeklyTrainingId;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
    }
}
