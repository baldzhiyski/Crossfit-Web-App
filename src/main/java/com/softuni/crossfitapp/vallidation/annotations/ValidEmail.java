package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.util.Constants;
import com.softuni.crossfitapp.vallidation.validators.ValidEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.softuni.crossfitapp.util.Constants.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Constraint(validatedBy = ValidEmailValidator.class)
public @interface ValidEmail {
    String message() default INVALID_EMAIL_FORMAT;
    String emptyMessage() default EMPTY_EMAIL;
    String invalidDomainMessage() default INVALID_DOMAIN_OPTION;
    String alreadyInUseMessage() default EMAIL_ALREADY_IN_USE;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedDomains() default {"abv.bg", "yahoo.com", "gmail.com"};
}