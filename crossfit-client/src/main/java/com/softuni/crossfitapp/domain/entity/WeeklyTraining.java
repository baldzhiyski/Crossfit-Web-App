package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "weekly_trainings")
@Getter
@Setter
@AllArgsConstructor
public class WeeklyTraining extends BaseEntity{

    @ManyToMany(mappedBy = "trainingsPerWeekList",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private List<User> participants;

    @Column
    private DayOfWeek dayOfWeek;

    @Column
    private String imageUrl;

    @Column
    private LocalTime time;

    @Column
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Level level;

    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;


    @ManyToOne
    private Coach coach;

    public WeeklyTraining(){
        this.participants = new ArrayList<>();
    }
}
