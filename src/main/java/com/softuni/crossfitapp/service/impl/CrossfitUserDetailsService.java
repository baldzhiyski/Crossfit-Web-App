package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.user_details.CrossfitUserDetails;
import com.softuni.crossfitapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class CrossfitUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    public CrossfitUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }

    private UserDetails map(com.softuni.crossfitapp.domain.entity.User userEntity) {
      return new CrossfitUserDetails(
              userEntity.getEmail(),
              userEntity.getPassword(),
              mapAuthorities(userEntity.getRoles()),
              userEntity.getFirstName(),
              userEntity.getLastName()
      );
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleType()))
                .collect(Collectors.toSet());
    }
}
