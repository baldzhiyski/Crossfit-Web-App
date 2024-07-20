package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {

    Coach findByFirstName(String firstName);

    @Query(value = "SELECT c FROM Coach c ORDER BY RAND() LIMIT 1", nativeQuery = false)
    Coach getRandomCoach();

    Optional<Coach> findByUsername(String username);
}
