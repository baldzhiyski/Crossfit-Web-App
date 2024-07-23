package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.sql.Types.VARCHAR;

@Entity
@Table(name = "weekly_trainings")
@Getter
@Setter
@AllArgsConstructor
public class WeeklyTraining extends BaseEntity{

    @ManyToMany(mappedBy = "trainingsPerWeekList",fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    private List<User> participants;

    @Column
    private DayOfWeek dayOfWeek;

    @Column
    private String imageUrl;

    @Column
    private LocalTime time;
    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;


    @Column
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Level level;

    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;


    @ManyToOne(cascade = CascadeType.MERGE)
    private Coach coach;

    public WeeklyTraining(){
        this.participants = new ArrayList<>();
    }
}
