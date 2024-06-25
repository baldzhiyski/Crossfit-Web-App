package com.softuni.crossfitapp.vallidation.annotations;


import com.softuni.crossfitapp.util.Constants;
import com.softuni.crossfitapp.vallidation.validators.PasswordMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.softuni.crossfitapp.util.Constants.NOT_MATCHING_PASSWORDS;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { PasswordMatchValidator.class })
public @interface PasswordMatch {

    String message() default NOT_MATCHING_PASSWORDS;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
