package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.users.LogInDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.entity.Country;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.events.UserRegisteredEvent;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.CountryRepository;
import com.softuni.crossfitapp.repository.RoleRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.util.CopyImageFileSaverUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private ApplicationEventPublisher applicationEventPublisher;
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private CountryRepository countryRepository;
    private ModelMapper mapper;

    public UserServiceImpl(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepository, RoleRepository roleRepository, CountryRepository countryRepository, ModelMapper mapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.mapper = mapper;
    }
    @Override
    public void registerNewUser(UserRegisterDto userRegisterDto) throws IOException {
        User toBeRegisteredUser = this.mapper.map(userRegisterDto, User.class);

        Country country = this.countryRepository.findByCode(userRegisterDto.getNationality()).orElseThrow(() -> new ObjectNotFoundException("No such code for country found !"));
        toBeRegisteredUser.setCountry(country);
        String imageUrl = CopyImageFileSaverUtil.saveFile(userRegisterDto.getPhoto());
        toBeRegisteredUser.setImageUrl(imageUrl);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleType(RoleType.USER));
        toBeRegisteredUser.setRoles(roles);

        this.userRepository.saveAndFlush(toBeRegisteredUser);

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(
                "UserService",
                userRegisterDto.getEmail(),
                userRegisterDto.getFullName()
        ));
    }

    @Override
    public void logInUser(LogInDto logInDto) {

    }
}
