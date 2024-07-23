package com.softuni.crossfitapp.domain.events;

import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class DisabledAccountEvent extends ApplicationEvent {

    private UUID uuid;
    private String username;
    private String fullName;
    private String email;
    public DisabledAccountEvent(Object source, UUID uuid, String username, String fullName, String email) {
        super(source);
        this.uuid = uuid;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }
}
