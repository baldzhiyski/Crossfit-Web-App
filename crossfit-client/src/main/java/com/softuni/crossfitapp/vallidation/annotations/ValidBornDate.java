package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.util.Constants;
import com.softuni.crossfitapp.vallidation.validators.ValidBornDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.softuni.crossfitapp.util.Constants.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidBornDateValidator.class)
public @interface ValidBornDate {
    String invalidBornDate() default "{invalid.born.date}";
    String notOldEnough() default "{not.old.enough}";
    String emptyMessage() default "{empty.born.date}";
    String message() default "{invalid.born.date}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

