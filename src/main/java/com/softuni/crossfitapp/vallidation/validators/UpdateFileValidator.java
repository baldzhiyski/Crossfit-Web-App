package com.softuni.crossfitapp.vallidation.validators;

import com.softuni.crossfitapp.util.AnnotationsUtil;
import com.softuni.crossfitapp.vallidation.annotations.ValidUpdateFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class UpdateFileValidator implements ConstraintValidator<ValidUpdateFile, MultipartFile>{
    private long maxSize;
    private String[] allowedTypes;
    private String invalidSize;

    private String invalidType;

    @Override
    public void initialize(ValidUpdateFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowedTypes = constraintAnnotation.allowedTypes();
        this.invalidSize = constraintAnnotation.fileSizeExceed();
        this.invalidType = constraintAnnotation.invalidType();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file == null){
            return true;
        }
        if(file.isEmpty()){
            return true;
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
