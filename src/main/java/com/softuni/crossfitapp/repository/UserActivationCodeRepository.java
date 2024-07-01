package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.UserActivationLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserActivationCodeRepository extends JpaRepository<UserActivationLinkEntity, UUID> {
    Optional<UserActivationLinkEntity> findByActivationCode(String code);
}
