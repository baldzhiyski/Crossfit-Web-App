package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.vallidation.annotations.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserRegisterDto> {

    private String message;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {

        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(UserRegisterDto userRegisterBindingModel, ConstraintValidatorContext context) {

        final String password = userRegisterBindingModel.getPassword();
        final String confirmPassword = userRegisterBindingModel.getConfirmPassword();

        boolean passwordMatch = password != null && password.equals(confirmPassword);

        if (!passwordMatch) {
            AnnotationsUtil.setErrorMessage(context,message);
            return false;
        }

        return true;
    }

}
