package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.vallidation.annotations.EmailUpdate;
import com.softuni.crossfitapp.vallidation.annotations.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.softuni.crossfitapp.util.Constants.EMAIL_REGEX;

public class ValidUpdateEmailValidator implements ConstraintValidator<EmailUpdate,String> {
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private String[] allowedDomains;

    private String alreadyInUseMessage;
    private String message;

    private String invalidEmailMessage;

    private UserRepository userRepository;

    @Autowired
    public ValidUpdateEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(EmailUpdate constraintAnnotation) {
        allowedDomains = constraintAnnotation.allowedDomains();
        message = constraintAnnotation.message();
        invalidEmailMessage = constraintAnnotation.invalidDomainMessage();
        alreadyInUseMessage = constraintAnnotation.alreadyInUseMessage();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true;
        }

        if(this.userRepository.findByEmail(email).isPresent()){
            AnnotationsUtil.setErrorMessage(context,alreadyInUseMessage);
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            AnnotationsUtil.setErrorMessage(context,message);
            return false;
        }

        // Check if domain is allowed
        String domain = email.substring(email.lastIndexOf("@") + 1);
        for (String allowedDomain : allowedDomains) {
            if (domain.equalsIgnoreCase(allowedDomain)) {
                return true;
            }
        }

        AnnotationsUtil.setErrorMessage(context,invalidEmailMessage);
        return false;
    }
}
