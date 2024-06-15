package com.softuni.crossfitapp.model.entity;

import com.softuni.crossfitapp.model.entity.enums.MembershipType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column
    @NotNull
    private Long duration;
}
