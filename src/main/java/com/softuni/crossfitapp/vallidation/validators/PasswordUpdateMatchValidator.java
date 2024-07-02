package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;

import com.softuni.crossfitapp.vallidation.annotations.PasswordUpdateMatcher;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordUpdateMatchValidator implements ConstraintValidator<PasswordUpdateMatcher, UserProfileUpdateDto> {
    @Override
    public boolean isValid(UserProfileUpdateDto userProfileUpdateDto, ConstraintValidatorContext constraintValidatorContext) {

        String password = userProfileUpdateDto.getPassword();
        String confirmPassword = userProfileUpdateDto.getConfirmPassword();

        if(password == null && confirmPassword == null){
            return true;
        }
        return password != null && password.equals(confirmPassword);

    }
}
