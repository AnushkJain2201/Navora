package com.navora.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ItineraryStopDto(
        UUID id,
        String landmarkName,
        String description,
        BigDecimal estimatedDurationHours,
        Integer orderIndex
) {}
