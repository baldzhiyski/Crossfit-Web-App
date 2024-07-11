package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.vallidation.annotations.ValidYouTubeUrl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.softuni.crossfitapp.util.Constants.YOUTUBE_URL_REGEX;

public class  YouTubeUrlValidator implements ConstraintValidator<ValidYouTubeUrl,String> {

    private String emptyMessage ;
    @Override
    public void initialize(ValidYouTubeUrl constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.emptyMessage=constraintAnnotation.emptyMessage();
    }

    @Override
    public boolean isValid(String videoUrl, ConstraintValidatorContext context) {
        if(videoUrl==null  || videoUrl.isEmpty()){
            AnnotationsUtil.setErrorMessage(context,emptyMessage);
            return false;
        }
        Pattern pattern = Pattern.compile(YOUTUBE_URL_REGEX);
        Matcher matcher = pattern.matcher(videoUrl);

        return matcher.matches();
    }
}
