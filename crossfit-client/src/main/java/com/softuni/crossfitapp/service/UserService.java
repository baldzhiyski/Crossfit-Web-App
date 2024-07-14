package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.users.UserProfileDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;

import java.io.IOException;
import java.util.Set;

public interface UserService {

    User registerNewUser(UserRegisterDto userRegisterDto) throws IOException;


    void activateAccount(String activationCode);

    UserProfileDto getProfilePageDto(String username);

    void updateProfile(String username, UserProfileUpdateDto userProfileUpdateDto) throws IOException;

    void deleteAcc(String username);

    void buyMembership(String loggedUserName, MembershipType membershipType);

    Set<User> findAllUsersWithExpiredMembership();

    void removeExpiredMembership(User user);
}
