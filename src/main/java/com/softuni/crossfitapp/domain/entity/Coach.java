package com.softuni.crossfitapp.domain.entity;

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
    private String firstName;

    @Column
    @NotBlank
    private String lastName;


    @OneToMany(mappedBy = "owner")
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private List<WeeklyTraining> trainingsPerWeek;

}
