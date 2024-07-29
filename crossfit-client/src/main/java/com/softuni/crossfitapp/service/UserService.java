package com.softuni.crossfitapp.service;

import com.softuni.crossfitapp.domain.dto.users.UserAdminPageDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    User registerNewUser(UserRegisterDto userRegisterDto) throws IOException;

    List<UserAdminPageDto> displayAllUsersAcc();

    void activateAccount(String activationCode);

    UserProfileDto getProfilePageDto(String username);

    void updateProfile(String username, UserProfileUpdateDto userProfileUpdateDto) throws IOException;

    void deleteAcc(String username);

    void buyMembership(String loggedUserName, MembershipType membershipType);

    Set<User> findAllUsersWithExpiredMembership();

    void removeExpiredMembership(User user);

    Optional<CrossfitUserDetails> getCurrentUser();

    boolean getLoggedUserCommentPermission(String username);

    void enableOrDisableAcc(UUID accountUUID,String action);

    void resetAllowedTrainingsPerWeek();

    Long getTotalMoney();

    Long numberAllActiveUsers();
}
