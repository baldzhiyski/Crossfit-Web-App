package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.vallidation.validators.ValidUpdateEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Constraint(validatedBy = ValidUpdateEmailValidator.class)
public @interface EmailUpdate {
    String message() default "{invalid.email.format}";
    String invalidDomainMessage() default "{invalid.domain.option}";
    String alreadyInUseMessage() default "{email.already.in.use}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedDomains() default {"abv.bg", "yahoo.com", "gmail.com"};
}
