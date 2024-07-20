package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.UUID;

import static java.sql.Types.VARCHAR;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity{

    @NotBlank
    private String eventName;

    @Lob
    @NotBlank
    private String description;

    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;

    @NotBlank
    private String address;

    @ManyToOne
    private User creator;
}