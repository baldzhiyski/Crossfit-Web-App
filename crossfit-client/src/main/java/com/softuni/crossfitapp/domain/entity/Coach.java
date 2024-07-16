package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.vallidation.annotations.UniqueUsername;
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
    @UniqueUsername
    private String username;

    @Column
    @NotBlank
    private String firstName;

    @Column
    @NotBlank
    private String lastName;


    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "coach", cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    private List<WeeklyTraining> trainingsPerWeek;

}
