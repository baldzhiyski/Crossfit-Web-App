package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.users.UserRegisterDto;
import com.softuni.crossfitapp.domain.entity.Country;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.UserActivationLinkEntity;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.*;
import com.softuni.crossfitapp.service.CloudinaryService;
import com.softuni.crossfitapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private CrossfitUserDetailsService serviceToTest;

    private UserService userService;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private UserActivationCodeRepository activationCodeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ModelMapper mapper;


    @BeforeEach
    void setUp() {
        serviceToTest = new CrossfitUserDetailsService(
                userRepository
        );
        userService = new UserServiceImpl(  applicationEventPublisher,  userRepository,  roleRepository,  membershipRepository,  countryRepository,  activationCodeRepository,
                passwordEncoder,  cloudinaryService,  mapper);
    }

    @Test
    void testUserNotFound() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("pesho@softuni.bg")
        );
    }
    @Test
    void testUserFoundException() {
        // Arrange
        User testUserEntity = createTestUser();
        when(userRepository.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        // Act
        UserDetails userDetails =
                serviceToTest.loadUserByUsername(testUserEntity.getUsername());

        // Assert
        assertNotNull(userDetails);
        assertEquals(
                testUserEntity.getUsername(),
                userDetails.getUsername(),
                "Different usernames !");

        assertEquals(testUserEntity.getPassword(), userDetails.getPassword());
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(
                containsAuthority(userDetails, "ROLE_" + RoleType.ADMIN),
                "The user is not admin");
        assertTrue(
                containsAuthority(userDetails, "ROLE_" + RoleType.USER),
                "The user is not user");
    }
    @Test
    void testUserSuccessfullyRegistered() throws IOException {
        // Arrange
        UserRegisterDto userToRegister = createUserToRegister();

        // Mock dependencies
        Country country = new Country();
        country.setCode("BG");
        when(countryRepository.findByCode("BG")).thenReturn(Optional.of(country));

        // Mock mapper to return a User entity when mapping from UserRegisterDto
        User expectedUserEntity = createTestUserFromDto(userToRegister);
        when(mapper.map(userToRegister, User.class)).thenReturn(expectedUserEntity);

        // Act
        User registeredUser = userService.registerNewUser(userToRegister);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(expectedUserEntity.getUsername(), registeredUser.getUsername());

        verify(userRepository).saveAndFlush(userCaptor.capture());

        User saved = userCaptor.getValue();
        assertEquals(userToRegister.getUsername(),saved.getUsername());
        assertEquals(userToRegister.getEmail(),saved.getEmail());

    }
    @Test
    void testActivateAccount() {
        // Arrange
        String activationCode = "testActivationCode";

        // Mock UserActivationLinkEntity and User
        UserActivationLinkEntity activationLinkEntity = new UserActivationLinkEntity();
        User user = new User();
        activationLinkEntity.setUserEntity(user);

        // Mock repository behavior

        when(activationCodeRepository.findByActivationCode(activationCode))
                .thenReturn(Optional.of(activationLinkEntity));
        when(roleRepository.findByRoleType(RoleType.USER))
                .thenReturn( Role.builder().roleType(RoleType.USER).build());

        // Act
        assertDoesNotThrow(() -> userService.activateAccount(activationCode));

        // Assert
        assertTrue(user.isActive());
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getRoleType() == RoleType.USER));

    }
    @Test
    public void disableAcc(){
        User userAcc = createTestUser();
        when(userRepository.findByUuid(userAcc.getUuid())).thenReturn(Optional.of(userAcc));
        assertFalse(this.userRepository.findByUuid(userAcc.getUuid()).get().isDisabled());

        this.userService.enableOrDisableAcc(userAcc.getUuid(),"disable");

        assertTrue(this.userRepository.findByUuid(userAcc.getUuid()).get().isDisabled());

    }

    @Test
    public void enableAcc(){
        User userAcc = createTestUser();
        when(userRepository.findByUuid(userAcc.getUuid())).thenReturn(Optional.of(userAcc));
        assertFalse(this.userRepository.findByUuid(userAcc.getUuid()).get().isDisabled());

        this.userService.enableOrDisableAcc(userAcc.getUuid(),"disable");

        assertTrue(this.userRepository.findByUuid(userAcc.getUuid()).get().isDisabled());

        this.userService.enableOrDisableAcc(userAcc.getUuid(),"enable");
        assertFalse(this.userRepository.findByUuid(userAcc.getUuid()).get().isDisabled());
    }

    @Test
    public void enableDisableAccShouldThrows(){
        User userAcc = createTestUser();
        when(userRepository.findByUuid(userAcc.getUuid())).thenReturn(Optional.of(userAcc));
        assertFalse(this.userRepository.findByUuid(userAcc.getUuid()).get().isDisabled());

        assertThrows(ObjectNotFoundException.class,()->{
            this.userService.enableOrDisableAcc(userAcc.getUuid(),"random");
        });

    }


    private boolean containsAuthority(UserDetails userDetails, String expectedAuthority) {
        return userDetails
                .getAuthorities()
                .stream()
                .anyMatch(a -> expectedAuthority.equals(a.getAuthority()));
    }

    private static User createTestUser() {
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setRoleType(RoleType.USER);
        roles.add(role);
        Role role2 = new Role();
        role2.setRoleType(RoleType.ADMIN);
        roles.add(role2);
        return  User.builder()
                .firstName("firstName")
                .isDisabled(false)
                .lastName("lastName")
                .username("shittyName")
                .uuid(UUID.randomUUID())
                .email("pesho@softuni.bg")
                .isActive(false)
                .password("topsecret")
                .roles(roles).build();
    }
    private static User createTestUserFromDto(UserRegisterDto userToRegister) {
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setRoleType(RoleType.USER);
        roles.add(role);

        return  User.builder()
                .firstName(userToRegister.getFirstName())
                .lastName(userToRegister.getLastName())
                .username(userToRegister.getUsername())
                .email(userToRegister.getEmail())
                .isActive(false)
                .password(userToRegister.getPassword())
                .roles(roles).build();
    }
    private static UserRegisterDto createUserToRegister() throws IOException {
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setRoleType(RoleType.USER);
        roles.add(role);


        return  UserRegisterDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .username("shittyNameSecond")
                .email("pesho@softuni.bg")
                .password("topsecret")
                .bornOn("01-02-2004")
                .confirmPassword("topsecret")
                .nationality("BG")
                .telephoneNumber("0899178929")
                .build();
    }

}