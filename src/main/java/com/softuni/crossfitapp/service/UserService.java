package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.users.LogInDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;

import java.io.IOException;

public interface UserService {

    void registerNewUser(UserRegisterDto userRegisterDto) throws IOException;


    void activateAccount(String activationCode);
}
