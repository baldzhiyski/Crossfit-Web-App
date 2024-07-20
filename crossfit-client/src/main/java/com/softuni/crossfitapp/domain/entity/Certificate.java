package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.Date;
import java.util.UUID;

import static java.sql.Types.VARCHAR;

@Entity
@Getter
@Setter
@Table(name = "certificates")
public class Certificate  extends BaseEntity{
    @NotBlank
    private String name;
    @Column(name = "obtained_on")
    @NotNull
    private Date obtainedOn;
    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;

    @ManyToOne
    private Coach owner;
}
