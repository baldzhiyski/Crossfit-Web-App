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

public class ValidBornDateValidator implements ConstraintValidator<ValidBornDate, Date> {

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
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            AnnotationsUtil.setErrorMessage(context,emptyMessage);
            return false;
        }

        LocalDate bornDate = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();

        if (bornDate.isAfter(now)) {
            AnnotationsUtil.setErrorMessage(context,invalidBornDateMessage);
            return false;
        }

        int age = Period.between(bornDate, now).getYears();
        if (age < 18) {
            AnnotationsUtil.setErrorMessage(context,notOldEnoughMessage);
            return false;
        }

        return true;
    }
}
}