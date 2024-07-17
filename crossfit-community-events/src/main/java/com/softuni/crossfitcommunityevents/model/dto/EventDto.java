package com.softuni.crossfitcommunityevents.model.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private String address;
    private String eventName;
    private String description;
    private Date date;

    private String videoUrl;

}
