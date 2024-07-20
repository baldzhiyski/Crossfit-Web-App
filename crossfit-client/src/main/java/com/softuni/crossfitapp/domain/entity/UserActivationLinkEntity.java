package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.sql.Types.VARCHAR;

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

    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;

}
