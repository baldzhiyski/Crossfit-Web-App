package com.softuni.crossfitapp.domain.dto.users;

import com.softuni.crossfitapp.vallidation.annotations.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Builder
@Getter
@PasswordMatch
public class UserRegisterDto {
    @NotBlank
    @UniqueUsername
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

   @NotBlank
    private String address;

    @NotBlank
    private String nationality;

   @NotBlank
    private String town;

    @ValidBornDate
    private Date bornOn;

    @PasswordAnnotation
    private String password;

    @PasswordAnnotation
    private String confirmPassword;

    @ValidFile
    private MultipartFile imageUrl;

    @PhoneNumber
    private String telephoneNumber;

    @ValidEmail
    private String email;
}
