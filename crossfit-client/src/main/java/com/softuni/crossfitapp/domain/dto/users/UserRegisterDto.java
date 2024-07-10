package com.softuni.crossfitapp.domain.dto.users;

import com.softuni.crossfitapp.vallidation.annotations.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@SuperBuilder
@Getter
@NoArgsConstructor
@Setter
@PasswordMatch
public class UserRegisterDto {
    @NotBlank(message = "{field.required}")
    @UniqueUsername
    private String username;

    @NotBlank(message = "{field.required}")
    private String firstName;

    @NotBlank(message = "{field.required}")
    private String lastName;

    @NotBlank(message = "{field.required}")
    private String address;

    @NotBlank(message = "{field.required}")
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


    public String getFullName(){
        return firstName + " " + lastName;
    }

}

