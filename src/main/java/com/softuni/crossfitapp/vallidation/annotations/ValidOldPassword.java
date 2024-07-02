package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.vallidation.validators.CorrectlyPasswordValidator;
import com.softuni.crossfitapp.vallidation.validators.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.softuni.crossfitapp.util.Constants.INVALID_PASS;

@Constraint(validatedBy = CorrectlyPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidOldPassword {
    String message() default INVALID_PASS;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
