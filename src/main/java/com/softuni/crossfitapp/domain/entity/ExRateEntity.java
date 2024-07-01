package com.softuni.crossfitapp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "ex_rates")
@Setter
@Getter
public class ExRateEntity extends BaseEntity {

    @NotEmpty
    @Column(unique = true)
    private String currency;

    @Positive
    @NotNull
    private BigDecimal rate;

    public ExRateEntity() {

    }


    @Override
    public String toString() {
        return "ExRateEntity{" +
                "currency='" + currency + '\'' +
                ", rate=" + rate +
                '}';
    }
}
