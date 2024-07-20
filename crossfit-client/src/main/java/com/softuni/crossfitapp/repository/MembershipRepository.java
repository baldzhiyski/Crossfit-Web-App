package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Membership;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByMembershipType(MembershipType membershipType);
}
