package com.softuni.crossfitapp.vallidation.annotations;

import com.softuni.crossfitapp.vallidation.validators.FileNotEmptyValidator;
import com.softuni.crossfitapp.vallidation.validators.UpdateFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.softuni.crossfitapp.util.Constants.*;
import static com.softuni.crossfitapp.util.Constants.MAX_FILE_SIZE;

@Constraint(validatedBy = UpdateFileValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUpdateFile {
    String message() default FILE_INVALID;
    String invalidType() default FILE_TYPE_NOT_ALLOWED;
    String fileSizeExceed() default FILE_SIZE_EXCEEDED;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    long maxSize() default MAX_FILE_SIZE; // Maximum file size in bytes

    String[] allowedTypes() default {"image/jpeg", "image/jpg","images/png"}; // Allowed MIME types
}
