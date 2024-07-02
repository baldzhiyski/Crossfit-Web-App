package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.users.UserProfileDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;

import java.io.IOException;

public interface UserService {

    void registerNewUser(UserRegisterDto userRegisterDto) throws IOException;


    void activateAccount(String activationCode);

    UserProfileDto getProfilePageDto(String username);

    void updateProfile(String username, UserProfileUpdateDto userProfileUpdateDto) throws IOException;
}
