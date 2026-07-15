package com.navora.backend.repository;

import com.navora.backend.entity.Trip;
import com.navora.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {

    List<Trip> findByUser(User user);
    List<Trip> findByUserId(UUID userId);
}
