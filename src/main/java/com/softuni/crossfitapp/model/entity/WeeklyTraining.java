package com.softuni.crossfitapp.model.entity;

import com.softuni.crossfitapp.model.entity.enums.DayOfWeek;
import com.softuni.crossfitapp.model.entity.enums.Level;
import com.softuni.crossfitapp.model.entity.enums.TrainingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
