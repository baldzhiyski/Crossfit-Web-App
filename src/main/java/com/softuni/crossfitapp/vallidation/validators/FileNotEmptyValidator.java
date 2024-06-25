package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.vallidation.annotations.ValidFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileNotEmptyValidator implements ConstraintValidator<ValidFile, MultipartFile> {


    private long maxSize;
    private String[] allowedTypes;

    private String emptyFile;


    private String invalidSize;

    private String invalidType;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowedTypes = constraintAnnotation.allowedTypes();
        this.emptyFile = constraintAnnotation.emptyMessage();
        this.invalidSize = constraintAnnotation.fileSizeExceed();
        this.invalidType = constraintAnnotation.invalidType();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            AnnotationsUtil.setErrorMessage(context,emptyFile);
            return false;
        }

        if (file.getSize() > maxSize) {
            AnnotationsUtil.setErrorMessage(context,invalidSize);
            return false;
        }

        if (allowedTypes.length > 0 && !Arrays.asList(allowedTypes).contains(file.getContentType())) {
           AnnotationsUtil.setErrorMessage(context,invalidType);
            return false;
        }

        return true;
    }
}
