package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.vallidation.validators.CorrectlyPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CorrectlyPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidOldPassword {
    String message() default "{invalid.pass}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
