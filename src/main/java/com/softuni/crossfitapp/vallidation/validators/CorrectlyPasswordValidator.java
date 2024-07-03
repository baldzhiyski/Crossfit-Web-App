package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.vallidation.annotations.ValidOldPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;


public class CorrectlyPasswordValidator implements ConstraintValidator<ValidOldPassword,CharSequence> {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CorrectlyPasswordValidator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isValid(CharSequence oldPassword, ConstraintValidatorContext constraintValidatorContext) {
        if(oldPassword == null){
            return false;
        }
        CrossfitUserDetails userDetails = (CrossfitUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        // Check if the old password matches the current stored password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        return true;
    }
}
