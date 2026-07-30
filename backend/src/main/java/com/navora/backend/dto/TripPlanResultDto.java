package com.navora.backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TripPlanResultDto(
        UUID tripId,
        String destination,
        Integer durationDays,
        BigDecimal budget,
        List<ItineraryDayDto> itineraryDays,
        String clarificationMessage
) {}