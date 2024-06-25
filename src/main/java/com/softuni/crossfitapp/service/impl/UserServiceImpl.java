package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.users.LogInDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.util.CopyFileSaverUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public void registerNewUser(UserRegisterDto userRegisterDto) throws IOException {
        User toBeRegisteredUser = this.mapper.map(userRegisterDto, User.class);
        String imageUrl = CopyFileSaverUtil.saveFile(userRegisterDto.getPhoto());
        toBeRegisteredUser.setImageUrl(imageUrl);



        this.userRepository.saveAndFlush(toBeRegisteredUser);
    }

    @Override
    public void logInUser(LogInDto logInDto) {

    }
}
