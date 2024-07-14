package com.softuni.crossfitapp.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDto {
    @NotBlank(message = "{payment.fullname.required}")
    private String fullName;

    @Email(message = "{payment.email.invalid}")
    @NotBlank(message = "{payment.email.required}")
    private String email;

    @NotBlank(message = "{payment.address.required}")
    private String address;

    @NotBlank(message = "{payment.country.required}")
    private String city;

    private String state;

    @NotBlank(message = "{payment.name.on.card.required}")
    private String cardName;

    @NotBlank(message = "{payment.card.number.required}")
    @Size(min = 16, max = 16, message = "{payment.card.number.size}")
    private String cardNumber;

    @NotBlank(message = "{payment.expiration.month.required}")
    private String expmonth;

    @NotBlank(message = "{payment.expiration.year.required}")
    private String expyear;

    @NotBlank(message = "{payment.cvv.required}")
    @Size(min = 3, max = 3, message = "{payment.cvv.size}")
    private String cvv;
}
