package com.softuni.crossfitcommunityevents.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity {
    @NotBlank
    @Size(min = 5)
    private String eventName;

    @NotBlank
    @Lob
    private String description;

    @NotBlank
    private String address;

    @NotBlank
    private Date date;

    @NotBlank
    private String videoUrl;
}
