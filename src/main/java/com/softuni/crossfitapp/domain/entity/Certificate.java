package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    @ManyToOne
    private Coach owner;
}
