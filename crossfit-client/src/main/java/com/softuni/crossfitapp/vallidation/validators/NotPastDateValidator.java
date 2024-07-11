package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.vallidation.annotations.NotPastDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;

public class NotPastDateValidator implements ConstraintValidator<NotPastDate, Date> {

    private String emptyMessage;
    @Override
    public void initialize(NotPastDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.emptyMessage = constraintAnnotation.emptyMessage();
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if(date==null){
            AnnotationsUtil.setErrorMessage(constraintValidatorContext,emptyMessage);
            return false;
        }
        Date today = new Date();
        return !date.before(today);
    }
}
