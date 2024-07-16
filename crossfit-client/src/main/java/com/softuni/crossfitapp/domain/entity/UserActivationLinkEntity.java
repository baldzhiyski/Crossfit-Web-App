package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activation_codes")
@Getter
@Setter
@NoArgsConstructor
public class UserActivationLinkEntity extends BaseEntity {

    private String activationCode;

    private Instant created;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User userEntity;
}
