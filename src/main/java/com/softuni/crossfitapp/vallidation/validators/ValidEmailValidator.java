package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.util.Constants;
import com.softuni.crossfitapp.vallidation.annotations.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.softuni.crossfitapp.util.Constants.emailRegex;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final Pattern pattern = Pattern.compile(emailRegex);

    private String[] allowedDomains;
    private String emptyMessage;
    private String message;

    private String invalidEmailMessage;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        allowedDomains = constraintAnnotation.allowedDomains();
        emptyMessage = constraintAnnotation.emptyMessage();
        message = constraintAnnotation.message();
        this.invalidEmailMessage = constraintAnnotation.invalidDomainMessage();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(emptyMessage).addConstraintViolation();
            return false;
        }

        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }

        // Check if domain is allowed
        String domain = email.substring(email.lastIndexOf("@") + 1);
        for (String allowedDomain : allowedDomains) {
            if (domain.equalsIgnoreCase(allowedDomain)) {
                return true;
            }
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(invalidEmailMessage).addConstraintViolation();
        return false;
    }
}