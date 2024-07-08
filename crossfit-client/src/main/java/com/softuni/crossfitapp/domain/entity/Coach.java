package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coach extends BaseEntity{

    @Column
    @NotBlank
    private String firstName;

    @Column
    @NotBlank
    private String lastName;


    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private List<WeeklyTraining> trainingsPerWeek;

}
