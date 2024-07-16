package com.softuni.crossfitapp.service.impl;

import com.google.gson.Gson;
import com.softuni.crossfitapp.domain.dto.coaches.SeedCoachDto;
import com.softuni.crossfitapp.domain.dto.memberships.SeedMembershipDto;
import com.softuni.crossfitapp.domain.dto.roles.SeedRoleDto;
import com.softuni.crossfitapp.domain.dto.users.SeedAdminDto;
import com.softuni.crossfitapp.domain.dto.users.SeedCoachesUserProfileDto;
import com.softuni.crossfitapp.domain.entity.*;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.*;
import com.softuni.crossfitapp.service.SeedService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softuni.crossfitapp.util.Constants.*;

@Component
public class SeedServiceImpl implements SeedService {
    private RoleRepository roleRepository;
    private CertificateRepository certificateRepository;

    private CoachRepository coachRepository;

    private UserRepository userRepository;

    private MembershipRepository membershipRepository;
    private Gson converter;
    private ModelMapper mapper;

    private CountryRepository countryRepository;

    public SeedServiceImpl(RoleRepository roleRepository, CertificateRepository certificateRepository, CoachRepository coachRepository, UserRepository userRepository, MembershipRepository membershipRepository, Gson converter, ModelMapper mapper, CountryRepository countryRepository) {
        this.roleRepository = roleRepository;
        this.certificateRepository = certificateRepository;
        this.coachRepository = coachRepository;
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.converter = converter;
        this.mapper = mapper;
        this.countryRepository = countryRepository;
    }

    @Override
    public void seedRoles() throws FileNotFoundException {
        if(this.roleRepository.count()==0){
            FileReader fileReader = new FileReader(PATH_TO_ROLES);

            Set<Role> roles = Arrays.stream(this.converter.fromJson(fileReader, SeedRoleDto[].class))
                    .map(rolesDto -> {
                        Role role = new Role();
                        role.setRoleType(rolesDto.getRoleType());
                        return role;
                    })
                    .collect(Collectors.toSet());

            this.roleRepository.saveAllAndFlush(roles);

        }
    }

    @Override
    public void seedCoaches() throws FileNotFoundException {
        if(this.coachRepository.count()==0){
            FileReader fileReader = new FileReader(PATH_TO_COACHES);

            List<Coach> coaches = Arrays.stream(this.converter.fromJson(fileReader, SeedCoachDto[].class))
                    .map(seedCoachDto -> {
                        Coach coach = new Coach();
                        coach.setFirstName(seedCoachDto.getFirstName());
                        coach.setLastName(seedCoachDto.getLastName());
                        coach.setUsername(seedCoachDto.getUsername());

                        List<Certificate> certificates = seedCoachDto.getCertificates().stream().map(certificateDto -> {
                            Certificate certificate = new Certificate();
                            certificate.setName(certificateDto.getName());
                            certificate.setObtainedOn(certificate.getObtainedOn());
                            certificate.setOwner(coach);
                            certificate.setObtainedOn(certificateDto.getObtainedOn());
                            return certificate;
                        }).collect(Collectors.toList());
                        coach.setCertificates(certificates);
                        return coach;
                    }).collect(Collectors.toList());

            this.coachRepository.saveAllAndFlush(coaches);
            coaches.forEach(coach -> this.certificateRepository.saveAllAndFlush(coach.getCertificates()));

        }
    }

    @Override
    public void seedMemberships() throws FileNotFoundException {
        if(this.membershipRepository.count()==0){
            FileReader fileReader = new FileReader(PATH_TO_MEMBERSHIPS);
            List<Membership> memberships = Arrays.stream(this.converter.fromJson(fileReader, SeedMembershipDto[].class))
                    .map(seedMembershipDto -> {
                        Membership membership = new Membership();
                        membership.setDuration(Long.valueOf(seedMembershipDto.getDuration()));
                        membership.setPrice(seedMembershipDto.getPrice());
                        membership.setMembershipType(seedMembershipDto.getMembershipType());
                        return membership;
                    }).collect(Collectors.toList());

            this.membershipRepository.saveAllAndFlush(memberships);
        }
    }

    @Override
    @Transactional
    public void seedCoachesUserAccount() throws FileNotFoundException {
        if(this.userRepository.count()==0){
            FileReader fileReader = new FileReader(PATH_TO_COACHES_USERS);

            List<User> coachesProfiles = Arrays.stream(this.converter.fromJson(fileReader, SeedCoachesUserProfileDto[].class))
                    .map(seedCoachesUserProfileDto -> {
                        User user = this.mapper.map(seedCoachesUserProfileDto, User.class);
                        Set<Role> roles = getRoles(seedCoachesUserProfileDto.getRoles());
                        user.setCountry(countryRepository.findByCode(seedCoachesUserProfileDto.getNationality()).orElseThrow(()-> new ObjectNotFoundException("No such country in the db !")));
                        user.setRoles(roles);
                        Membership byMembershipType = this.membershipRepository.findByMembershipType(seedCoachesUserProfileDto.getMembership().getMembershipType());
                        user.setMembership(byMembershipType);
                        return user;
                    }).collect(Collectors.toList());

            this.userRepository.saveAllAndFlush(coachesProfiles);
        }
    }


    @Override
    @Transactional
    public void seedAdmins() throws FileNotFoundException {
        FileReader fileReader = new FileReader(PATH_TO_ADMINS);
        List<User> admins = Arrays.stream(this.converter.fromJson(fileReader, SeedAdminDto[].class))
                .map(seedAdminDto -> {
                    User user = this.mapper.map(seedAdminDto, User.class);
                    Set<Role> roles = getRoles(seedAdminDto.getRoles());
                    user.setCountry(countryRepository.findByCode(seedAdminDto.getNationality()).orElseThrow(()-> new ObjectNotFoundException("No such country in the db !")));
                    user.setRoles(roles);
                    return user;
                }).collect(Collectors.toList());
        this.userRepository.saveAllAndFlush(admins);

    }

    private Set<Role> getRoles(Set<SeedRoleDto> roleTypeSet) {
        Set<Role> roles = new HashSet<>();
        roleTypeSet.forEach(roleType1 -> {
            Role byRoleType = this.roleRepository.findByRoleType(roleType1.getRoleType());
            roles.add(byRoleType);
        });
        return roles;
    }

}
