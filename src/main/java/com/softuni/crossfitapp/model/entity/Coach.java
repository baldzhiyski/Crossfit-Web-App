package com.softuni.crossfitapp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@NoArgsConstructor
public class Coach extends BaseEntity{
    @Column
    @NotBlank
    private String fullName;

    @Column
    @NotBlank
    private String description;

    @Column
    @NotBlank
    private String imageUrl;

    @ManyToMany
    private List<Certificate> certificateList;

    @OneToMany(mappedBy = "coach")
    private List<WeeklyTraining> trainingsPerWeek;

}
