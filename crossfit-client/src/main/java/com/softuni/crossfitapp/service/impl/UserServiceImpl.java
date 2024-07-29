package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.events.EventDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipProfilePageDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDto;
import com.softuni.crossfitapp.domain.dto.users.UserAdminPageDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.entity.*;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.events.DisabledAccountEvent;
import com.softuni.crossfitapp.domain.events.EnabledAccountEvent;
import com.softuni.crossfitapp.domain.events.UserRegisteredEvent;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.*;
import com.softuni.crossfitapp.service.CloudinaryService;
import com.softuni.crossfitapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private ApplicationEventPublisher applicationEventPublisher;
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private MembershipRepository membershipRepository;

    private CountryRepository countryRepository;

    private UserActivationCodeRepository activationCodeRepository;

    private PasswordEncoder passwordEncoder;

    private CloudinaryService cloudinaryService;
    private ModelMapper mapper;

    public UserServiceImpl(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepository, RoleRepository roleRepository, MembershipRepository membershipRepository, CountryRepository countryRepository, UserActivationCodeRepository activationCodeRepository, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService, ModelMapper mapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.membershipRepository = membershipRepository;
        this.countryRepository = countryRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.mapper = mapper;
    }

    @Override
    public User registerNewUser(UserRegisterDto userRegisterDto) throws IOException {
        User toBeRegisteredUser = this.mapper.map(userRegisterDto, User.class);

        Country country = this.countryRepository.findByCode(userRegisterDto.getNationality()).orElseThrow(() -> new ObjectNotFoundException("No such code for country found !"));
        toBeRegisteredUser.setCountry(country);
        String imageUrl = cloudinaryService.uploadPhoto(userRegisterDto.getPhoto(), "users-accounts-photos");
        toBeRegisteredUser.setImageUrl(imageUrl);

        // Setting the role when we click on the confirm url

        this.userRepository.saveAndFlush(toBeRegisteredUser);

        applicationEventPublisher.publishEvent(new UserRegisteredEvent(
                "UserService",
                userRegisterDto.getEmail(),
                userRegisterDto.getFullName()
        ));
        return toBeRegisteredUser;
    }

    @Override
    public List<UserAdminPageDto> displayAllUsersAcc() {
        return this.userRepository.findAll()
                .stream()
                .map(user -> this.mapper.map(user, UserAdminPageDto.class))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void activateAccount(String activationCode) {

        UserActivationLinkEntity userActivationLinkEntity = this.activationCodeRepository.findByActivationCode(activationCode).orElseThrow(() -> new ObjectNotFoundException("No such code in the db"));

        User user = userActivationLinkEntity.getUserEntity();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleType(RoleType.USER));
        user.setRoles(roles);
        user.setActive(true);
        userActivationLinkEntity.setUserEntity(user);
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public UserProfileDto getProfilePageDto(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("No such user existing !"));
        String fullName = user.getFirstName() + " " + user.getLastName();
        Set<RoleType> roles = user.getRoles().stream().map(Role::getRoleType).collect(Collectors.toSet());
        MembershipProfilePageDto mapped;
        if (user.getMembership() != null) {
            mapped = this.mapper.map(user.getMembership(), MembershipProfilePageDto.class);

            long daysLeft = ChronoUnit.DAYS.between(user.getMembershipStartDate(), user.getMembershipEndDate());
            mapped.setTimeLeft(daysLeft);

        } else {
            mapped = new MembershipProfilePageDto();
            mapped.setTimeLeft(0L);
            mapped.setMembershipType(MembershipType.NONE);
        }
        Set<TrainingDto> enrolledTrainingsNames = user.getTrainingsPerWeekList().stream().map(weeklyTraining -> {
            return this.mapper.map(weeklyTraining, TrainingDto.class);
        }).collect(Collectors.toSet());

        return new UserProfileDto(
                fullName, user.getUsername(), user.getImageUrl(), user.getUuid(), user.getEmail(), user.getAddress(), roles, mapped, enrolledTrainingsNames
        );
    }

    @Override
    @Transactional
    public void updateProfile(String username, UserProfileUpdateDto userProfileUpdateDto) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with username: " + username));

        // Update only the fields that are not null or not empty in the DTO
        if (userProfileUpdateDto.getUsername() != null && !userProfileUpdateDto.getFirstName().isBlank()) {
            user.setFirstName(userProfileUpdateDto.getFirstName());
        }
        if (userProfileUpdateDto.getLastName() != null && !userProfileUpdateDto.getLastName().isBlank()) {
            user.setLastName(userProfileUpdateDto.getLastName());
        }
        if (userProfileUpdateDto.getEmail() != null && !userProfileUpdateDto.getEmail().isBlank()) {
            user.setEmail(userProfileUpdateDto.getEmail());
        }
        if (userProfileUpdateDto.getAddress() != null && !userProfileUpdateDto.getAddress().isBlank()) {
            user.setAddress(userProfileUpdateDto.getAddress());
        }
        if (userProfileUpdateDto.getDateOfBirth() != null && !userProfileUpdateDto.getDateOfBirth().isBlank()) {

            LocalDate dateOfBirth = LocalDate.parse(userProfileUpdateDto.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // Convert LocalDate to Instant
            Instant instant = dateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant();

            // Convert Instant to Date
            Date dateOfBirthDate = Date.from(instant);

            user.setBornOn(dateOfBirthDate);
        }

        if (userProfileUpdateDto.getPicture() != null && !userProfileUpdateDto.getPicture().isEmpty()) {
            // Delete old photo from Cloudinary
            String oldImageUrl = user.getImageUrl();
            if (oldImageUrl != null) {
                String publicId = oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1);
                cloudinaryService.deletePhoto(publicId);
            }

            // Upload new photo to Cloudinary
            String imageUrl = cloudinaryService.uploadPhoto(userProfileUpdateDto.getPicture(), "users-accounts-photos");
            user.setImageUrl(imageUrl);
        }


        if (userProfileUpdateDto.getPassword() != null && !userProfileUpdateDto.getPassword().isBlank() && !userProfileUpdateDto.getConfirmPassword().isBlank()) {
            user.setPassword(this.passwordEncoder.encode(userProfileUpdateDto.getPassword()));
        }

        if (userProfileUpdateDto.getUsername() != null && !userProfileUpdateDto.getUsername().isBlank()) {
            user.setUsername(userProfileUpdateDto.getUsername());
        }
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteAcc(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        this.activationCodeRepository.deleteAll(this.activationCodeRepository.findAllByUserEntity_Id(user.getId()));
        // Delete the user
        this.userRepository.delete(user);
        // Logout the current user
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    @Transactional
    public void buyMembership(String loggedUserName, MembershipType membershipType) {
        Membership byMembershipType = this.membershipRepository.findByMembershipType(membershipType).orElseThrow();
        User user = this.userRepository.findByUsername(loggedUserName).orElseThrow(() -> new ObjectNotFoundException("Something went wrong with the logged user !"));

        user.setMembership(byMembershipType);
        user.setMembershipDuration(membershipType);
        Role byRoleType = this.roleRepository.findByRoleType(RoleType.MEMBER);
        user.getRoles().add(byRoleType);

        int maxTrainingSessionsPerWeek = getMaxTrainingSessionsPerWeekDependinOnMembershipType(byMembershipType);
        user.setWeeklyTrainingsCount(maxTrainingSessionsPerWeek);
        this.userRepository.saveAndFlush(user);

    }


    @Override
    public Set<User> findAllUsersWithExpiredMembership() {
        return this.userRepository.findAllUsersWithExpiredMembership(LocalDate.now());
    }

    @Override
    @Transactional
    public void removeExpiredMembership(User user) {
        user.setMembership(null);
        user.setMembershipStartDate(null);
        user.setMembershipEndDate(null);
        user.setRoles(Set.of(this.roleRepository.findByRoleType(RoleType.USER)));
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<CrossfitUserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CrossfitUserDetails userDetails = (CrossfitUserDetails) authentication.getPrincipal();
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    @Override
    public boolean getLoggedUserCommentPermission(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("Something went wrong with the logged user !")).isDisabled();
    }


    // TODO : Add event
    @Override
    public void enableOrDisableAcc(UUID accountUUID, String action) {
        User user = this.userRepository.findByUuid(accountUUID).orElseThrow(() -> new ObjectNotFoundException("Something went wrong with the logged user !"));
        switch (action) {
            case "enable" ->{
                user.setDisabled(false);
                applicationEventPublisher.publishEvent(new EnabledAccountEvent(
                        "UserService",accountUUID,user.getUsername(),user.getFullName(),user.getEmail()
                ));

            }
            case "disable" ->{
                user.setDisabled(true);
                applicationEventPublisher.publishEvent(new DisabledAccountEvent(
                        "UserService",accountUUID,user.getUsername(),user.getFullName(),user.getEmail()
                ));
            }
            default -> throw new ObjectNotFoundException("Invalid action");
        }
        this.userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void resetAllowedTrainingsPerWeek() {
        List<User> allUsers = this.userRepository.findAll().stream().toList();
        for (User user : allUsers) {
            Membership membership = user.getMembership();
            int maxTrainingSessionsPerWeekDependinOnMembershipType = getMaxTrainingSessionsPerWeekDependinOnMembershipType(membership);
            if (user.getWeeklyTrainingsCount() != maxTrainingSessionsPerWeekDependinOnMembershipType) {
                user.setWeeklyTrainingsCount(maxTrainingSessionsPerWeekDependinOnMembershipType);
                this.userRepository.saveAndFlush(user);
            }
        }
    }

    @Override
    public Long getTotalMoney() {
        return this.userRepository.findAll()
                .stream()
                .filter(user -> !user.getRoles().contains(this.roleRepository.findByRoleType(RoleType.ADMIN)))
                .map(User::getMembership)
                .map(Membership::getPrice)// Extract the price
                .reduce(0L, Long::sum); // Sum up all prices
    }

    @Override
    public Long numberAllActiveUsers() {
        return this.userRepository.findAll()
                .stream()
                .filter(User::isActive)
                .count();
    }

    private static int getMaxTrainingSessionsPerWeekDependinOnMembershipType(Membership byMembershipType) {
        int maxTrainingSessionsPerWeek;
        switch (byMembershipType.getMembershipType()) {
            case BASIC -> maxTrainingSessionsPerWeek = 3;
            case ELITE -> maxTrainingSessionsPerWeek = 7;
            case PREMIUM -> maxTrainingSessionsPerWeek = 5;
            case UNLIMITED -> maxTrainingSessionsPerWeek = 24;
            default -> throw new ObjectNotFoundException("Invalid membership type when trying to enroll for training");
        }
        return maxTrainingSessionsPerWeek;
    }
}
