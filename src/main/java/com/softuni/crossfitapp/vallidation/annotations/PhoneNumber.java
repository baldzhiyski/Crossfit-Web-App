package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.vallidation.validators.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.softuni.crossfitapp.util.Constants.*;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String defaultMessage() default INVALID_PHONE_NUMBER_FORMAT;
    String emptyMessage() default EMPTY_PHONE;
    String uniqueMessage() default UNIQUE_PHONE_REQUIRED;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}