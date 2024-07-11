package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.events.EventDto;
import com.softuni.crossfitapp.domain.dto.memberships.MembershipProfilePageDto;
import com.softuni.crossfitapp.domain.dto.trainings.TrainingDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileDto;
import com.softuni.crossfitapp.domain.dto.users.UserProfileUpdateDto;
import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.entity.Country;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.UserActivationLinkEntity;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.events.UserRegisteredEvent;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.CountryRepository;
import com.softuni.crossfitapp.repository.RoleRepository;
import com.softuni.crossfitapp.repository.UserActivationCodeRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.CloudinaryService;
import com.softuni.crossfitapp.service.UserService;
import com.softuni.crossfitapp.util.CopyImageFileSaverUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private ApplicationEventPublisher applicationEventPublisher;
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private CountryRepository countryRepository;

    private UserActivationCodeRepository activationCodeRepository;

    private PasswordEncoder passwordEncoder;

    private CloudinaryService cloudinaryService;
    private ModelMapper mapper;

    public UserServiceImpl(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepository, RoleRepository roleRepository, CountryRepository countryRepository, UserActivationCodeRepository activationCodeRepository, CloudinaryService cloudinaryService, ModelMapper mapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.activationCodeRepository = activationCodeRepository;
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
    @Transactional
    public void activateAccount(String activationCode) {

        UserActivationLinkEntity userActivationLinkEntity = this.activationCodeRepository.findByActivationCode(activationCode).orElseThrow(() -> new ObjectNotFoundException("No such code in the db"));

        User user = userActivationLinkEntity.getUser();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleType(RoleType.USER));
        user.setRoles(roles);
        user.setActive(true);
        userActivationLinkEntity.setUser(user);
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public UserProfileDto getProfilePageDto(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("No such user existing !"));
        String fullName = user.getFirstName()+ " " + user.getLastName();
        Set<RoleType> roles = user.getRoles().stream().map(Role::getRoleType).collect(Collectors.toSet());
        MembershipProfilePageDto mapped;
        if(user.getMembership()!=null) {
             mapped = this.mapper.map(user.getMembership(), MembershipProfilePageDto.class);

            long daysLeft = ChronoUnit.DAYS.between(user.getMembershipStartDate(), user.getMembershipEndDate());
            mapped.setTimeLeft(daysLeft);

        }else{
            mapped= new MembershipProfilePageDto();
            mapped.setTimeLeft(0L);
            mapped.setMembershipType(MembershipType.NONE);
        }
        Set<EventDto> events = user.getEvents().stream().map(event -> {
            return this.mapper.map(event, EventDto.class);
        }).collect(Collectors.toSet());

        Set<TrainingDto> enrolledTrainingsNames = user.getTrainingsPerWeekList().stream().map(weeklyTraining -> {
            return this.mapper.map(weeklyTraining, TrainingDto.class);
        }).collect(Collectors.toSet());

        return new UserProfileDto(
                fullName,user.getUsername(),user.getImageUrl(),user.getId(),user.getEmail(),user.getAddress(),roles,mapped,events, enrolledTrainingsNames
        );
    }

    @Override
    @Transactional
    public void updateProfile(String username, UserProfileUpdateDto userProfileUpdateDto) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with username: " + username));

        // Update only the fields that are not null or not empty in the DTO
        if (!userProfileUpdateDto.getFirstName().isBlank()) {
            user.setFirstName(userProfileUpdateDto.getFirstName());
        }
        if (!userProfileUpdateDto.getLastName().isBlank()) {
            user.setLastName(userProfileUpdateDto.getLastName());
        }
        if (!userProfileUpdateDto.getEmail().isBlank()) {
            user.setEmail(userProfileUpdateDto.getEmail());
        }
        if (!userProfileUpdateDto.getAddress().isBlank()) {
            user.setAddress(userProfileUpdateDto.getAddress());
        }
        if (!userProfileUpdateDto.getDateOfBirth().isBlank()) {

            LocalDate dateOfBirth = LocalDate.parse(userProfileUpdateDto.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // Convert LocalDate to Instant
            Instant instant = dateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant();

            // Convert Instant to Date
            Date dateOfBirthDate = Date.from(instant);

            user.setBornOn(dateOfBirthDate);
        }

        if(!userProfileUpdateDto.getPicture().isEmpty()){
            if (!userProfileUpdateDto.getPicture().isEmpty()) {
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

        }

        if(!userProfileUpdateDto.getPassword().isBlank() && !userProfileUpdateDto.getConfirmPassword().isBlank()){
            user.setPassword(this.passwordEncoder.encode(userProfileUpdateDto.getPassword()));
        }

        if(!userProfileUpdateDto.getUsername().isBlank()){
            user.setUsername(userProfileUpdateDto.getUsername());
        }
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteAcc(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        // Delete the user
        this.userRepository.delete(user);
        // Logout the current user
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
