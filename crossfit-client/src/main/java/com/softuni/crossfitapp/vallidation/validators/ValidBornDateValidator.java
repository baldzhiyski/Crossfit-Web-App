package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.vallidation.annotations.ValidBornDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static com.softuni.crossfitapp.util.AnnotationsUtil.setErrorMessage;

public class ValidBornDateValidator implements ConstraintValidator<ValidBornDate, String> {

    private String invalidBornDateMessage;
    private String notOldEnoughMessage;
    private String emptyMessage;

    @Override
    public void initialize(ValidBornDate constraintAnnotation) {
        this.invalidBornDateMessage = constraintAnnotation.invalidBornDate();
        this.notOldEnoughMessage = constraintAnnotation.notOldEnough();
        this.emptyMessage = constraintAnnotation.emptyMessage();

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            setErrorMessage(context, emptyMessage);
            return false;
        }

        try {
            LocalDate bornDate = LocalDate.parse(value);

            LocalDate now = LocalDate.now();

            if (bornDate.isAfter(now)) {
                setErrorMessage(context, invalidBornDateMessage);
                return false;
            }

            int age = Period.between(bornDate, now).getYears();
            if (age < 18) {
                setErrorMessage(context, notOldEnoughMessage);
                return false;
            }

            return true;
        } catch (Exception e) {
            setErrorMessage(context, invalidBornDateMessage);
            return false;
        }
    }
}