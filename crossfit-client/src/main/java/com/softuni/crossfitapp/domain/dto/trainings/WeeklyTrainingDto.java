package com.softuni.crossfitapp.domain.dto.trainings;

import com.softuni.crossfitapp.domain.entity.Coach;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class WeeklyTrainingDto {


    private List<User> participants;

    private UUID id;

    private TrainingType trainingType;

    private DayOfWeek dayOfWeek;

    private LocalTime time;

    private String coachFirstName;

    private String imageUrl;

    private String coachLastName;

    private LocalDate date;

    private String coachUsername;

    public List<String> getParticipantsUsernames(){
        return this.participants.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.date.format(formatter);
    }

}
