package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.vallidation.validators.NotPastDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotPastDateValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotPastDate {
    String emptyMessage() default "{field.required}";
    String message() default "{date.not.in.past}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}