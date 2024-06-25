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
    String invalidBornDate() default INVALID_BORN_DATE;
    String notOldEnough() default NOT_OLD_ENOUGH;
    String emptyMessage() default EMPTY_BORN_DATE;
    String message() default INVALID_BORN_DATE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

