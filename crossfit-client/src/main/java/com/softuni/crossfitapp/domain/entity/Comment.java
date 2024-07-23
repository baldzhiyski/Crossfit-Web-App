package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.sql.Types.VARCHAR;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity{
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private User author;

    @Column
    @NotBlank
    private String text;

    @NotNull
    private LocalDate date;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.ALL})
    private Training training;

    @Column(nullable = false)
    private Integer likes;

    @Column(nullable = false)
    private Integer dislikes ;

    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;


    @ManyToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likedBy ;

    @ManyToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JoinTable(
            name = "comment_dislikes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> dislikedBy;

}
