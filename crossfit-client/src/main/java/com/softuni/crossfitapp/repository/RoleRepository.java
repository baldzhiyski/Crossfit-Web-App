package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByRoleType(RoleType roleType);
}
