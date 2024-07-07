package com.softuni.crossfitapp.repository;

import com.softuni.crossfitapp.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event,UUID>{
}