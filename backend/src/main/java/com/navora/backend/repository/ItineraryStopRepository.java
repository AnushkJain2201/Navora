package com.navora.backend.repository;

import com.navora.backend.entity.ItineraryStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItineraryStopRepository extends JpaRepository<ItineraryStop, UUID> {
}
