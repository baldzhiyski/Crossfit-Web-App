package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.vallidation.validators.YouTubeUrlValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = YouTubeUrlValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYouTubeUrl {
    String emptyMessage() default "{field.required}";
    String message() default "{youtube.url.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}