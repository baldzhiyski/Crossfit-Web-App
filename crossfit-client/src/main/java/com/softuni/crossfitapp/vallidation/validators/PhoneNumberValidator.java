package com.softuni.crossfitapp.vallidation.validators;


import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.vallidation.annotations.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import static com.softuni.crossfitapp.util.Constants.PHONE_NUMBER_PATTERN;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private String alreadyInUse;

    private UserRepository userRepository;

    @Autowired
    public PhoneNumberValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        alreadyInUse = constraintAnnotation.uniqueMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(this.userRepository.findByTelephoneNumber(value).isPresent()){
            AnnotationsUtil.setErrorMessage(context,alreadyInUse);
            return false;
        }

         return true;
    }
}