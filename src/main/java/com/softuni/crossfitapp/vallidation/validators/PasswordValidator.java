package com.softuni.crossfitapp.vallidation.validators;


import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.util.Constants;
import com.softuni.crossfitapp.vallidation.annotations.PasswordAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<PasswordAnnotation, CharSequence> {

    private int minLength;
    private int maxLength;
    private boolean hasUpper;
    private boolean hasLower;
    private boolean hasDigit;
    private boolean hasSpecialSymbol;

    @Override
    public void initialize(PasswordAnnotation password) {
        this.minLength = password.minLength();
        this.maxLength = password.maxLength();
        this.hasUpper = password.containsUpperCase();
        this.hasLower = password.containsLowerCase();
        this.hasDigit = password.containsDigit();
        this.hasSpecialSymbol = password.containsSpecialSymbols();
    }

    @Override
    public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
        if (value == null) {
            AnnotationsUtil.setErrorMessage(context, Constants.PASSWORD_CANNOT_BE_NULL);
            return false;
        }

        if (value.length() < this.minLength) {
            AnnotationsUtil.setErrorMessage(context, Constants.PASSWORD_TOO_SHORT);
            return false;
        }

        if (value.length() > this.maxLength) {
            AnnotationsUtil.setErrorMessage(context, Constants.PASSWORD_TOO_LONG);
            return false;
        }

        String password = value.toString();

        if (!Constants.PATTERN_LOWER.matcher(password).find() && this.hasLower) {
            AnnotationsUtil.setErrorMessage(context, Constants.PASSWORD_SHOULD_CONTAIN_LOWERCASE_LETTER);
            return false;
        }

        if (!Constants.PATTERN_UPPER.matcher(password).find() && this.hasUpper) {
            AnnotationsUtil.setErrorMessage(context, Constants.PASSWORD_SHOULD_CONTAIN_UPPERCASE_LETTER);
            return false;
        }

        if (!Constants.PATTERN_DIGIT.matcher(password).find() && this.hasDigit) {
            AnnotationsUtil.setErrorMessage(context, Constants.PASSWORD_SHOULD_CONTAIN_DIGIT);
            return false;
        }

        if (!Constants.PATTERN_SYMBOL.matcher(password).find() && this.hasSpecialSymbol) {
            AnnotationsUtil.setErrorMessage(context, Constants.PASSWORD_SHOULD_CONTAIN_SPECIAL_SYMBOL);
            return false;
        }

        return true;
    }
}