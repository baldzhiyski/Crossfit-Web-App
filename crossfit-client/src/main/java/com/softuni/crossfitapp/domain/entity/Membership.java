package com.softuni.crossfitapp.domain.entity;

import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.UUID;

import static java.sql.Types.VARCHAR;

@Entity
@Table(name = "memberships")
@Getter
@Setter
@NoArgsConstructor
public class Membership extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    @Column
    private Long price;

    @UUIDSequence
    @JdbcTypeCode(VARCHAR)
    private UUID uuid;


    @Column
    @NotNull
    private Long duration;
}
