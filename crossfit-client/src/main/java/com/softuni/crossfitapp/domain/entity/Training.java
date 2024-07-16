package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.domain.entity.enums.Level;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
@Table(name = "trainings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Training extends BaseEntity{

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String description;

    @Column
    @NotBlank
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Level level;

    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;

    @OneToMany(mappedBy = "training")
    private Set<Comment> comments;
}
