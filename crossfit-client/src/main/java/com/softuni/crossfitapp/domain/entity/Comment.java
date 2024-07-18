package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity{
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private User author;

    @Column
    @NotBlank
    private String text;

    @NotNull
    private LocalDate date;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Training training;

    @Column(nullable = false)
    private Integer likes;

    @Column(nullable = false)
    private Integer dislikes ;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy ;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "comment_dislikes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> dislikedBy;

}
