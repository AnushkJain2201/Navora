package com.navora.backend.repository;

import com.navora.backend.entity.ItineraryDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItineraryDayRepository extends JpaRepository<ItineraryDay, UUID> {
}
