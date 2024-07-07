package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity{
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @Column
    @NotBlank
    private String text;

    @NotNull
    private LocalDate date;

    @ManyToOne
    private Training training;
}
