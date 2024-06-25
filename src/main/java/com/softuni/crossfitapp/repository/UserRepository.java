package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.service.impl.CrossfitUserDetailsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByTelephoneNumber(String telephone);
}
