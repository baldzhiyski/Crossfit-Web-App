package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.service.impl.CrossfitUserDetailsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByTelephoneNumber(String telephone);

    Optional<User> findByUuid(UUID uuid);

    Set<User> findByRolesContaining(Role role);

    List<User> findAllByRolesContaining(Role byRoleType);

    Optional<User> findByUsernameOrEmail(String username,String email);
    @Query("SELECT DISTINCT u FROM User u  WHERE u.membershipEndDate <= :today")
    Set<User> findAllUsersWithExpiredMembership(LocalDate today);
}
