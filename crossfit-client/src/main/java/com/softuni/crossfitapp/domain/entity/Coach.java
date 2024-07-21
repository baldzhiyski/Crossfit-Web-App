package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.vallidation.annotations.UniqueUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.List;
import java.util.UUID;

import static java.sql.Types.VARCHAR;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coach extends BaseEntity{

    @Column
    private String username;

    @Column
    @NotBlank
    private String firstName;

    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;


    @Column
    @NotBlank
    private String lastName;


    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "coach", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<WeeklyTraining> trainingsPerWeek;

}
