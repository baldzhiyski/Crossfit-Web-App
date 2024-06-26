package com.softuni.crossfitapp.domain.dto.users;

import com.softuni.crossfitapp.vallidation.annotations.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static com.softuni.crossfitapp.util.Constants.FIELD_ACCEPTED;
import static com.softuni.crossfitapp.util.Constants.FIELD_REQUIRED;

@Getter
@NoArgsConstructor
@Setter
@PasswordMatch
public class UserRegisterDto {
    @NotBlank(message = FIELD_REQUIRED)
    @UniqueUsername
    private String username;

    @NotBlank(message = FIELD_REQUIRED)
    private String firstName;

    @NotBlank(message = FIELD_REQUIRED)
    private String lastName;

    @NotBlank(message = FIELD_REQUIRED)
    private String address;

    @NotBlank(message = FIELD_REQUIRED)
    private String nationality;


    @ValidBornDate
    private String bornOn;

    @PasswordAnnotation
    private String password;

    @PasswordAnnotation
    private String confirmPassword;

    @ValidFile
    private MultipartFile photo;

    @PhoneNumber
    private String telephoneNumber;

    @ValidEmail
    private String email;

}

