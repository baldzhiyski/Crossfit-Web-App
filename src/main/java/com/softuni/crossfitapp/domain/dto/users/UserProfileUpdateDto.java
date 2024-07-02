package com.softuni.crossfitapp.domain.dto.users;

import com.softuni.crossfitapp.vallidation.annotations.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@PasswordUpdateMatcher
public class UserProfileUpdateDto {

    private String firstName;
    private String lastName;

    @EmailUpdate
    private String email;

    private String address;

    private String dateOfBirth;

    @ValidUpdateFile
    private MultipartFile picture;


    @ValidOldPassword
    private String currentPassword;

    @PasswordUpdateAnnotation
    private String password;

    @PasswordUpdateAnnotation
    private String confirmPassword;
}
