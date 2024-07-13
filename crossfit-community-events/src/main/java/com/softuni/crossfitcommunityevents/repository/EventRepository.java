package com.softuni.crossfitcommunityevents.repository;

import com.softuni.crossfitcommunityevents.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    @Query("SELECT  e FROM Event e ORDER BY RAND() desc")
    List<Event> findSomeRandomEvents();

    Optional<Event> findByEventName(String eventName);
}