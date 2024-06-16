package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.domain.entity.enums.DayOfWeek;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "weekly_trainings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyTraining extends Training{

    @ManyToOne
    private User participant;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @NotNull
    private Date date;


    @ManyToOne
    private Coach coach;

}
