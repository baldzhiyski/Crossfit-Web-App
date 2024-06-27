package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.users.LogInDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.repository.RoleRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.util.CopyImageFileSaverUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;
    private ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }
    @Override
    public void registerNewUser(UserRegisterDto userRegisterDto) throws IOException {
        User toBeRegisteredUser = this.mapper.map(userRegisterDto, User.class);
        String imageUrl = CopyImageFileSaverUtil.saveFile(userRegisterDto.getPhoto());
        toBeRegisteredUser.setImageUrl(imageUrl);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleType(RoleType.USER));
        toBeRegisteredUser.setRoles(roles);

        this.userRepository.saveAndFlush(toBeRegisteredUser);
    }

    @Override
    public void logInUser(LogInDto logInDto) {

    }
}
