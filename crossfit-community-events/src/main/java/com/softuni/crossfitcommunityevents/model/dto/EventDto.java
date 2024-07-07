package com.softuni.crossfitcommunityevents.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class EventDto {

    private String address;
    private String eventName;
    private String description;
    private Date date;

}
