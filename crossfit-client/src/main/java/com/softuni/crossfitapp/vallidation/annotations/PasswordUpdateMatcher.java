package com.softuni.crossfitapp.vallidation.annotations;
import com.softuni.crossfitapp.vallidation.validators.PasswordUpdateMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { PasswordUpdateMatchValidator.class })
public @interface PasswordUpdateMatcher {
    String message() default "{not.matching.passwords}";


    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
