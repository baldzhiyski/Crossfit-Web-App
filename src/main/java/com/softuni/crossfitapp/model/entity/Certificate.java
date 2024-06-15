package com.softuni.crossfitapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "certificates")
public class Certificate  extends BaseEntity{
    @NotBlank
    private String name;
    @Column(name = "obtained_on")
    @NotNull
    private Date obtainedOn;
}
