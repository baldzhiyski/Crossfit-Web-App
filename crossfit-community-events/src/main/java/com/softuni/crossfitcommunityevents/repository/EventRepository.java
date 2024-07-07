package com.softuni.crossfitcommunityevents.repository;

import com.softuni.crossfitcommunityevents.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findTop3ByOrderByDateDesc();

}
